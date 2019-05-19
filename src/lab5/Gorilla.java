package lab5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Gorilla {
	private final static String TESTDATA_DIR = "C:\\Users\\Arvid Mildner\\Documents\\edaf05-workspace-2019\\edaf05\\5gorilla\\data\\secret\\";
	// private final static String TESTDATA_DIR =
	// "C:/Users/Arvid/Documents/edaf05-projects/edaf05-2019/5gorilla/data/secret/";
	static String[] alphabet;

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(TESTDATA_DIR + "0mini.in"));
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
		int l1 = qList.get(1).s1.length();
		int l2 = qList.get(1).s2.length();
		int[][] memory;
		// System.out.println(opt(l1-1,l2-1, costs, memory = new int [l1][l2],
		// qList.get(1)));
		String s1 = "SX";
		String s2 = "X";
		int n = s1.length();
		int m = s2.length();
		memory = new int[n+m+1][n+m+1];
		
	    for (i = 0; i <= (n + m); i++) 
	    { 
	        memory[i][0] = i * -4; 
	        memory[0][i] = i * -4; 
	    }
	    
		System.out.println(opt(s1.length()-1, s2.length()-1, costs,
				memory,
				new Query(new String[] { s1, s2 })));
		
		System.out.println(Arrays.deepToString(memory));
		System.out.println(memory.length);
	}

	private static String buildStrings(int[][] mem) {
		int i = 0;
		int j= 0;
		/*
		while(i<mem.length && j< mem[0].length) {
		
		}
		*/
		
		return "fo";
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
		if (i == 0 && j == 0) {
			return getPairingCost(i, j, costs, q);
		}
		if (i == 0) {
			return -4 * j;
		}
		if (j == 0) {
			return -4 * i;
		}
		if (memory[i][j] != 0) {
			return memory[i][j];
		} else {
			int pairingCost = getPairingCost(i, j, costs, q);
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
