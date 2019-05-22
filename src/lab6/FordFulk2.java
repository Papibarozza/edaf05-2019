package lab6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class FordFulk2 {
	private final static String TESTDATA_DIR = "C:\\Users\\Arvid Mildner\\Documents\\edaf05-workspace-2019\\edaf05\\6railwayplanning\\data\\secret\\";
	private static HashSet<Integer> globalVisited;
	// private final static String TESTDATA_DIR =
	// "C:/Users/Arvid/Documents/edaf05-projects/edaf05-2019/6railwayplanning/data/secret/";

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(TESTDATA_DIR + "4huge.in"));
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
			// System.out.println(Arrays.toString(nodeParams));
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
			Node.Edge eU = u.addEdge(v.id, cost);
			Node.Edge eV = v.addEdge(u.id, cost);
			eU.backwardsEdge = eV;
			eV.backwardsEdge = eU;
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
		int src = 0;
		int sink = n - 1;
		int routesRemoved = 0;
		int oldFlow = 0;
		int ij = 0;
		fordFulkerson(src, sink, nodes);
		int totalFlow = totalFlow(nodes, sink);
		for (int route : removes) {
			int[] connection = connections[route]; // Get the route to remove
			if (globalVisited.contains(connection[0]) && !globalVisited.contains(connection[1])) {
				totalFlow = totalFlow - connection[2]; // Then just reduce by capacity of this connection.
				nodes = removeConnection(connection[0], connection[1], nodes);
			}

			if (totalFlow < totalCapacity) {
				/*
				 * The amount of routes removed before we got too low flow, And that old flow
				 */
				System.out.println(routesRemoved + " " + oldFlow);
				break;
			}
			oldFlow = totalFlow;
			routesRemoved++;
		}

	}

	private static Node[] resetFlows(Node[] nodes) {
		for (Node n : nodes) {
			n.resetFlows();
		}
		return nodes;
	}

	private static int totalFlow(Node[] nodes, int sinkID) {
		int totalFlow = 0;
		for (Node n1 : nodes) {
			if (n1.id != sinkID) {
				for (Node.Edge edge : n1.edges) {
					totalFlow += edge.flow;

				}
			}
		}
		return totalFlow;
	}

	private static void fordFulkerson(int src, int sink, Node[] nodes) {
		Path p;
		while ((p = findPath(src, sink, nodes)) != null) {
			findDeltaAndUpdate(p, nodes);
		}

	}

	private static void findDeltaAndUpdate(Path path, Node[] nodes) {
		Path p1 = path;
		int delta = Integer.MAX_VALUE;
		while (p1.prev != null && p1.edge != null) {
			Node.Edge e = p1.edge;
			delta = Math.min(delta, e.cost - e.flow);
			p1 = p1.prev;
		}

		while (path.prev != null && path.edge != null) {
			// If current edge connects along this path, increase it.
			Node.Edge e = path.edge;
			e.flow = e.flow + delta;
			Node.Edge backwardsEdge = e.backwardsEdge;
			backwardsEdge.flow = backwardsEdge.flow - delta;
			path = path.prev;
		}
	}

	/*
	 * private static int findDelta(Path path) { while (path.prev != null) { path =
	 * path.prev; } }
	 */

	private static Node[] removeConnection(int u, int v, Node[] nodes) {
		nodes[u].removeEdge(v);
		nodes[v].removeEdge(u);
		return nodes;

	}

	private static Path findPath(int u, int v, Node[] nodes) {
		ArrayDeque<Path> q = new ArrayDeque<>();
		HashSet<Integer> visited = new HashSet<Integer>();
		globalVisited = null;
		Path start = new Path(nodes[u], null, null);
		q.offer(start);
		visited.add(u);
		while (!q.isEmpty()) {
			Path currentPath = q.pop();
			for (Node.Edge edge : currentPath.node.edges) {
				Node currentNode = nodes[edge.endPoint];
				if (!visited.contains(currentNode.id) && edge.flow < edge.cost) {
					Path newPath = new Path(currentNode, currentPath, edge); // What edge brought the path to this node.
					visited.add(currentNode.id);
					if (edge.endPoint == v) {
						globalVisited = visited;
						return newPath; // Found the path
					} else {
						q.push(newPath); // DFS
					}
				}

			}
		}
		globalVisited = visited;
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
		Node node;
		Node.Edge edge;

		Path(Node n, Path prev, Node.Edge edge) {
			this.node = n;
			this.prev = prev;
			this.edge = edge;
		}

	}

	private static class Node {
		int id;
		ArrayList<Edge> edges = new ArrayList<>();

		Node(int u) {
			this.id = u;
		}

		public void resetFlows() {
			for (Node.Edge e : edges) {
				e.flow = 0;
			}

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

		public Edge addEdge(int v, int cost) {
			Edge e = new Edge(this.id, v, cost);
			this.edges.add(e);
			return e;
		}

		private static class Edge {
			public Node.Edge backwardsEdge;
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
				return startPoint + " <- " + cost + "|" + flow + " -> " + endPoint;

			}
		}
	}

}
