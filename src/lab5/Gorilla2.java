package lab5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Gorilla2 {
	private final static String TESTDATA_DIR = "C:\\Users\\Arvid Mildner\\Documents\\edaf05-workspace-2019\\edaf05\\5gorilla\\data\\secret\\";
	// private final static String TESTDATA_DIR =
	// "C:/Users/Arvid/Documents/edaf05-projects/edaf05-2019/5gorilla/data/secret/";
	static String[] alphabet;

	public static void main(String[] args) throws IOException {
		//BufferedReader in = new BufferedReader(new FileReader(TESTDATA_DIR + "2med.in"));
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		alphabet = in.readLine().split("\\s+");
		int dims = alphabet.length;
		int[][] costs = new int[dims][dims];
		int i = 0;
		while (i < dims) {
			int[] row = mapToInt(in.readLine().split("\\s+"));
			costs[i] = row;
			i++;
		}
		in.readLine(); // Skip number of queries
		ArrayList<Query> qList = new ArrayList<>();
		String queryString;
		while ((queryString = in.readLine()) != null) {
			qList.add(new Query(queryString.split("\\s")));
		}

		for (Query q : qList) {
			String s1 = q.s1;
			String s2 = q.s2;
			int n = s1.length();
			int m = s2.length();
			int[][] memory = new int[n + m + 1][m + n + 1];

			for (i = 0; i <= n; i++) {
				memory[i][0] = i * -4;
			}
			for (i = 0; i <= m; i++) {
				memory[0][i] = i * -4;
			}

			opt(n, m, costs, memory, q);
			System.out.println(buildStrings(memory, costs, q));
		}
	}

	private static String buildStrings(int[][] mem, int[][] costs, Query q) {
		String s1 = q.s1;
		String s2 = q.s2;
		int n = s1.length();
		int m = s2.length();
		int l = n + m;
		int pgap = -4;
		int i = n;
		int j = m;

		int xpos = l - 1;
		int ypos = l - 1;

		char string1[] = new char[l];
		char string2[] = new char[l];

		while (!(i == 0 || j == 0)) {
			if (mem[i - 1][j - 1] + getPairingCost(i - 1, j - 1, costs, q) == mem[i][j]) {
				string1[xpos--] = s1.charAt(i - 1);
				string2[ypos--] = s2.charAt(j - 1);
				i--;
				j--;
			} else if (mem[i - 1][j] + pgap == mem[i][j]) {
				string1[xpos--] = s1.charAt(i - 1);
				string2[ypos--] = '*';
				i--;
			} else if (mem[i][j - 1] + pgap == mem[i][j]) {
				string2[ypos--] = s2.charAt(j - 1);
				string1[xpos--] = '*';
				j--;
			}
		}
		while (xpos > 0) {
			if (i > 0)
				string1[xpos--] = s1.charAt(--i);
			else
				string1[xpos--] = '*';
		}
		while (ypos > 0) {
			if (j > 0)
				string2[ypos--] = s2.charAt(--j);
			else
				string2[ypos--] = '*';
		}

		int startPos = getStartPos(l, string1, string2);

		StringBuilder sb = new StringBuilder();
		for (i = startPos; i <= l - 1; i++) {
			sb.append(string1[i]);
		}
		sb.append(" ");
		for (i = startPos; i <= l - 1; i++) {
			sb.append(string2[i]);
		}
		return sb.toString();
	}

	private static int getStartPos(int l, char[] string1, char[] string2) {

		int id = 1;
		for (int i = l - 1; i >= 1; i--) {
			if (string2[i] == '*' && string1[i] == '*') {
				id = i + 1;
				break;
			}
		}
		return id;
	}

	private static class Query {
		private String s1;
		private String s2;

		Query(String[] query) {

			this.s1 = query[0];
			this.s2 = query[1];
		}

		public String toString() {
			return s1 + " " + s2;
		}
	}

	private static int[] mapToInt(String[] xy) {
		int[] intArr = new int[xy.length];
		int i = 0;
		for (String s : xy) {
			intArr[i++] = Integer.parseInt(s);
		}
		return intArr;
	}

	public static int opt(int i, int j, int[][] costs, int[][] memory, Query q) {
		if (i == 1 && j == 1) {
			memory[i][j] = Math.max(-4, getPairingCost(i - 1, j - 1, costs, q));
			return Math.max(-4, getPairingCost(i - 1, j - 1, costs, q));
		}
		if (memory[i][j] != 0) {
			return memory[i][j];
		} else {
			int pairingCost = getPairingCost(i - 1, j - 1, costs, q);
			int opt1 = pairingCost + opt(i - 1, j - 1, costs, memory, q);
			int opt2 = -4 + opt(i, j - 1, costs, memory, q);
			int opt3 = -4 + opt(i - 1, j, costs, memory, q);
			int optimal = Math.max(opt1, Math.max(opt2, opt3));
			memory[i][j] = optimal;
			return optimal;
		}

	}

	private static int getPairingCost(int i, int j, int[][] costs, Query q) {
		String c1 = Character.toString((q.s1).charAt(i));
		String c2 = Character.toString((q.s2).charAt(j));
		int idx1 = getIndexOf(c1, alphabet);
		int idx2 = getIndexOf(c2, alphabet);
		return costs[idx1][idx2];
	}

	private static int getIndexOf(String c, String[] arr) {
		int i = 0;
		for (String chr : arr) {
			if (chr.equals(c)) {
				return i;
			}
			i++;
		}
		return -1;
	}

}
