package lab3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Prim {
	private final static String TESTDATA_DIR = "C:\\Users\\Arvid Mildner\\Documents\\edaf05-workspace-2019\\edaf05\\3makingfriends\\data\\secret\\";

	public static void main(String[] args) throws FileNotFoundException {
		//Scanner sc = new Scanner(new File(TESTDATA_DIR + "2med.in"));
		Scanner sc = new Scanner(System.in);
		int[][] graph = generateGraph(sc);
		PriorityQueue<Node> q = new PriorityQueue<>(
				(a, b) -> a.keyValue < b.keyValue ? -1 : a.keyValue == b.keyValue ? 0 : 1);
		Node[] nodeList = new Node[graph.length];
		initialize(q, nodeList, graph);
		q.peek().keyValue = 0;

		ArrayDeque<Node> mst = new ArrayDeque<>();

		while (!(q.peek()==null)) {
			Node currentVertex = q.poll();
			mst.push(currentVertex);
			
			for (int neighbor : currentVertex.neighbors) {
				if (nodeList[neighbor] != null) {
					int weight = graph[currentVertex.idx][neighbor];
					int keyValue = nodeList[neighbor].keyValue;
					if (weight < keyValue) {
						nodeList[neighbor].keyValue = weight;
						q.remove(nodeList[neighbor]);
						q.add(nodeList[neighbor]);
					}
				}
			}
			nodeList[currentVertex.idx] = null;
			
			/*
			for (int i = 0; i < graph.length; i++) {
				if(graph[currentVertex.idx][i]!=0) {
					int weight = graph[currentVertex.idx][i];
					int keyValue = nodeList[i].keyValue;
					if (weight < keyValue) {
						nodeList[i].keyValue = weight;
						q.remove(nodeList[i]);
						q.add(nodeList[i]);
					}
					graph[currentVertex.idx][i]=0;
					graph[i][currentVertex.idx] = 0;
				}
			}
			*/
			
		}
		int totalCost = 0;
		while (!mst.isEmpty()) {
			totalCost += mst.pop().keyValue;
		}
		System.out.println(totalCost);

	}

	private static void initialize(PriorityQueue<Node> q, Node[] nodeList, int[][] graph) {
		for (int i = 1; i < graph.length; i++) {
			Node currentNode = new Node(i);
			q.add(currentNode);
			for (int j = 1; j < graph[i].length; j++) {
				if (graph[i][j] != 0) {
					currentNode.neighbors.add(j);
					
					nodeList[i] = currentNode;
				}
			}
		}
	}

	public static int[][] generateGraph(Scanner sc) {
		int n = sc.nextInt();
		//Skip m
		sc.nextInt();

		int[][] graph = new int[n + 1][n + 1];
		while (sc.hasNext()) {
			int v1 = sc.nextInt();
			int v2 = sc.nextInt();
			int w = sc.nextInt();
			graph[v1][v2] = w;
			graph[v2][v1] = w;
		}

		return graph;
	}

}
