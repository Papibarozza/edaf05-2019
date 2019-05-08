package lab3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Scanner;

import lab3.Node.Edge;

public class Prim2 {
	private final static String TESTDATA_DIR = "C:\\Users\\Arvid Mildner\\Documents\\edaf05-workspace-2019\\edaf05\\3makingfriends\\data\\secret\\";

	public static void main(String[] args) throws FileNotFoundException {
		//Scanner sc = new Scanner(new File(TESTDATA_DIR + "4huge.in"));

		Scanner sc = new Scanner(System.in);
		//long startTime = System.nanoTime();   
		FastPriorityQueue<Node> q = new FastPriorityQueue<>(
				(a, b) -> a.keyValue < b.keyValue ? -1 : a.keyValue == b.keyValue ? 0 : 1);
		int n = sc.nextInt();
		int m = sc.nextInt();
		Node[] nodeList = new Node[n + 1]; //To not have to think about index offset

		initialize(q, nodeList, sc);
		q.peek().keyValue = 0;

		ArrayDeque<Node> mst = new ArrayDeque<>();

		while (!(q.peek() == null)) {
			Node currentVertex = q.poll();
			mst.push(currentVertex);

			for (Node.Edge edge : currentVertex.edgeNeighbors) {
				int neighbor = edge.endPoint;
				if (nodeList[neighbor] != null) {
					int weight = edge.weight;
					int keyValue = nodeList[neighbor].keyValue;
					if (weight < keyValue) {
						nodeList[neighbor].keyValue = weight;
						q.remove(nodeList[neighbor]);
						q.offer(nodeList[neighbor]);
					}
				}
			}
			nodeList[currentVertex.idx] = null;
			//System.out.println(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());
		}
		int totalCost = 0;
		while (!mst.isEmpty()) {
			totalCost += mst.pop().keyValue;
		}
		System.out.println(totalCost);
		//long estimatedTime = System.nanoTime() - startTime;
		//System.out.println("Time elapsed: "+ Long.toString(estimatedTime/1000000)+"ms");
	}

	public static void initialize(FastPriorityQueue<Node> q, Node[] nodeList, Scanner sc) {
		while (sc.hasNext()) {
			int v1 = sc.nextInt();
			int v2 = sc.nextInt();
			int w = sc.nextInt();
			Node nodeV1;
			Node nodeV2;
			if (nodeList[v1] == null) {
				nodeV1 = new Node(v1);
				nodeList[v1] = nodeV1;
				q.add(nodeV1);
			} else {
				nodeV1 = nodeList[v1];
			}

			if (nodeList[v2] == null) {
				nodeV2 = new Node(v2);
				q.add(nodeV2);
				nodeList[v2] = nodeV2;
			} else {
				nodeV2 = nodeList[v2];
			}
			nodeV1.addEdge(v2, w);
			nodeV2.addEdge(v1, w);

		}

	}

}
