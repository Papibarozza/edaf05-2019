package lab6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class FordFulk {
	// private final static String TESTDATA_DIR = "C:\\Users\\Arvid
	// Mildner\\Documents\\edaf05-workspace-2019\\edaf05\\5gorilla\\data\\secret\\";
	private final static String TESTDATA_DIR = "C:/Users/Arvid/Documents/edaf05-projects/edaf05-2019/6railwayplanning/data/secret/";

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(TESTDATA_DIR + "0mini.in"));
		int[] params = mapToInt(in.readLine().split("\\s+"));
		int n = params[0];
		int m = params[1];
		int totalCapacity = params[2];
		int p = params[3];
		Node[] nodes = new Node[n];
		int[][] connections = new int[m][3];

		int i = 0;
		while (i < m) {
			int[] nodeParams = mapToInt(in.readLine().split("\\s"));
			connections[i] = nodeParams;
			System.out.println(Arrays.toString(nodeParams));
			int uIdx = nodeParams[0];
			int vIdx = nodeParams[1];
			Node u;
			Node v;
			if (nodes[uIdx] == null) {
				u = new Node(uIdx);
			} else {
				u = nodes[uIdx];
			}
			if (nodes[vIdx] == null) {
				v = new Node(vIdx);
			} else {
				v = nodes[vIdx];
			}
			int cost = nodeParams[2];
			u.addEdge(v.id, cost);
			v.addEdge(u.id, cost);
			nodes[u.id] = u;
			nodes[v.id] = v;

			i++;
		}
		int[] removes = new int[p];
		i = 0;
		while (i < p) {
			removes[i] = Integer.valueOf(in.readLine());
			i++;
		}
		for (Node node : nodes) {
			for (Node.Edge e : node.edges) {
				System.out.println(e);
			}
		}
		Path path = findPath(1, 4, nodes);
		//int delta = findDelta(path);
		while (path.prev != null) {
			System.out.println(path.prev.wayPoint.id);
			path = path.prev;
		}

	}

	/*private static int findDelta(Path path) {
		while (path.prev != null) {
			path = path.prev;
		}
	}
	*/

	private static Node[] removeConnection(int u, int v, Node[] nodes) {
		nodes[u].removeEdge(v);
		nodes[v].removeEdge(u);
		return nodes;

	}

	private static Path findPath(int u, int v, Node[] nodes) {
		ArrayDeque<Path> q = new ArrayDeque<>();
		HashSet<Integer> visited = new HashSet<Integer>();
		Path start = new Path(nodes[u], null);
		q.offer(start);
		visited.add(u);
		while (!q.isEmpty()) {
			Path currentPath = q.pop();
			for (Node.Edge edge : currentPath.wayPoint.edges) {
				Node currentNode = nodes[edge.endPoint];
				if (!visited.contains(edge.endPoint) && edge.flow < edge.cost) {
					Path newPath = new Path(currentNode, currentPath);
					if (edge.endPoint == v) {
						return newPath; // Found the path
					} else {
						q.offer(newPath);
					}
				}
				visited.add(edge.endPoint);
			}
		}

		return null;
	}

	private static int[] mapToInt(String[] xy) {
		int[] intArr = new int[xy.length];
		int i = 0;
		for (String s : xy) {
			intArr[i++] = Integer.parseInt(s);
		}
		return intArr;
	}

	private static class Path {
		Path prev;
		Node wayPoint;

		Path(Node n, Path prev) {
			this.wayPoint = n;
			this.prev = prev;
		}
	}

	private static class Node {
		int id;
		ArrayList<Edge> edges = new ArrayList<>();

		Node(int u) {
			this.id = u;
		}

		public void removeEdge(int v) {
			int i = 0;
			for (Edge e : edges) {
				if (e.endPoint == v) {
					edges.remove(i);
					break;
				}
				i++;
			}

		}

		public Node addEdge(int v, int cost) {
			this.edges.add(new Edge(this.id, v, cost));
			return this;
		}

		private static class Edge {
			int startPoint;
			int endPoint;
			int cost;
			int flow;
			int delta;

			Edge(int u, int v, int cost) {
				this.startPoint = u;
				this.endPoint = v;
				this.cost = cost;
			}

			public void updateFlow(int additionalFlow) {
				this.flow += additionalFlow;
				this.delta = this.cost - this.flow;
				if (delta < 0) {
					throw new Error("Delta is negative");
				}
			}

			@Override
			public String toString() {
				return startPoint + " <-" + cost + "-> " + endPoint;

			}
		}
	}

}
