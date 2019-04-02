package lab2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class WordLadders {

	static class Node {
		int prev;
		int idx;
		int dist;

		Node(int idx, int prev, int dist) {
			this.idx = idx;
			this.dist = dist;
			this.prev = prev;

		}
	}

	static class Pair {
		String start;
		String end;

		Pair(String start, String end) {
			this.start = start;
			this.end = end;
		}

		@Override
		public String toString() {
			return start + " " + end;

		}
	}

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int q = sc.nextInt();
		String[] words = new String[n];
		int lineNumber = 0;
		sc.nextLine();
		while (lineNumber < n) {
			words[lineNumber] = sc.nextLine();
			lineNumber++;
		}
		int i = 0;
		Pair[] testQueries = new Pair[q];
		while (i < q) {		
			testQueries[i] = new Pair(sc.next(),sc.next());
			i += 1;
		}
		//long startTime = System.nanoTime();
		
		ArrayList<Integer>[] graph = generateTopology(words);
		
/*		long endTime = System.nanoTime();
		long duration = (endTime - startTime)/1000000; 
		System.out.println("Init time: "+ duration+"ms");
		
		long startTime2 = System.nanoTime();*/
		for (Pair p : testQueries) {
			int res = testQuery(p.start, p.end, graph, words);
			if (res == -1) {
				System.out.println("Impossible");
			} else {
				System.out.println(res);
			}
		}
		
/*		long endTime2 = System.nanoTime();
		long duration2 = (endTime2 - startTime2)/1000000; 
		System.out.println("Query time: "+ duration2+"ms");*/

	}

	static int testQuery(String testStr, String targetStr, ArrayList<Integer>[] graph, String[] words) {
		int testIdx = getIndex(testStr, words);
		int targetIdx = getIndex(targetStr, words);
		int pathlength = shortestPath(testIdx, targetIdx, graph);

		return pathlength;
	}

	private static int shortestPath(int startIdx, int targetIdx, ArrayList<Integer>[] graph) {

		ArrayDeque<Node> q = new ArrayDeque<>();
		q.offer(new WordLadders.Node(startIdx, 0, 0));
		return bfs(q, targetIdx, graph, new HashSet<Integer>());
	}

	private static int bfs(ArrayDeque<Node> q, int target, ArrayList<Integer>[] graph, HashSet<Integer> visited) {
		visited.add(q.peek().idx);
		while (!q.isEmpty()) {
			Node currentNode = q.pop();
			if (currentNode.idx == target) {
				return currentNode.dist;
			}
			for (int neighbor : graph[currentNode.idx]) {

				if (!visited.contains(neighbor)) {
					q.offer(new WordLadders.Node(neighbor, currentNode.idx, currentNode.dist + 1));
					visited.add(neighbor);
				}
			}

		}

		return -1;

	}

	private static int getIndex(String testStr, String[] words) {
		for (int i = 0; i < words.length; i++) {
			if (words[i].equals(testStr)) {
				return i;
			}
		}
		return -1;// not found
	}

	static ArrayList<Integer>[] generateTopology(String[] words) {
		ArrayList<Integer>[] topology = new ArrayList[words.length];
		for (int i = 0; i < words.length; i++) {
			topology[i] = new ArrayList<Integer>();
			for (int j = 0; j < words.length; j++) {
				if (hasValidEdge(words[i], words[j])) {
					topology[i].add(j);
				}
			}

		}

		return topology;
	}

	static boolean[][] createEdgeMatrix(String[] words) {
		boolean[][] edgeMatrix = new boolean[words.length][words.length];
		for (int i = 0; i < words.length; i++) {
			for (int j = 0; j < words.length; j++) {

				edgeMatrix[i][j] = hasValidEdge(words[i], words[j]);
				edgeMatrix[j][i] = hasValidEdge(words[j], words[i]);

			}
		}

		return edgeMatrix;
	}

	private static boolean hasValidEdge(String u, String v) {
		// If string u is of length 1 it returns true.
		String teststr = u.substring(1, u.length());
		char[] testChars = teststr.toCharArray();
		char[] chars = v.toCharArray();
		boolean foundMatch;

		for (char testChar : testChars) {
			foundMatch = false;
			for (int i = 0; i < chars.length; i++) {
				if (testChar == chars[i]) {
					chars[i] = 0;
					foundMatch = true;
					break;
				}
			}
			if (!foundMatch) {
				return false;
			}

		}
		return true;
	}

}
