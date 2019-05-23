package lab6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class FordFulk {
	//private final static String TESTDATA_DIR = "C:\\Users\\Arvid Mildner\\Documents\\edaf05-workspace-2019\\edaf05\\6railwayplanning\\data\\secret\\";
	 private final static String TESTDATA_DIR =
    "C:/Users/Arvid/Documents/edaf05-projects/edaf05-2019/6railwayplanning/data/secret/";

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
			u.addEdge(v.id, cost);
			v.addEdge(u.id, cost);
			nodes[u.id] = u;
			nodes[v.id] = v;

			i++;
		}
		int[] routes = new int[p];
		i = 0;
		while (i < p) {
			routes[i] = Integer.valueOf(in.readLine());
			i++;
		}
		int src = 0;
		int sink = n - 1;
		int totalFlow=0;
		//int routesToRemove = p/2;
		int l = 0;
		int r = p-1;
		int routesToRemove=0;
		Node [] originalNodes = nodes.clone();
		while (l<=r) {
			routesToRemove = (l+r)/2;
			nodes = originalNodes;
			nodes = removeConnections(routesToRemove, routes, connections, nodes);
			totalFlow = calculateFlows(nodes, src, sink);
			if(totalFlow>totalCapacity){
				l = routesToRemove+1;
			}else if(totalFlow<totalCapacity){
				r = routesToRemove-1;
			}else{
				break;
			}
		}
		System.out.println(routesToRemove+" "+totalFlow);
	}

	private static int calculateFlows(Node[] nodes, int src, int sink) {
		nodes = resetFlows(nodes);
		fordFulkerson(src, sink, nodes);
		return totalFlow(nodes, sink);
	}

	private static Node[] removeConnections(int nmbrOfroutes, int[] routes, int[][] connections, Node[] nodes) {
		for (int i = 0; i <= nmbrOfroutes; i++) {
			int[] connection = connections[routes[i]];
			nodes = removeConnection(connection[0], connection[1], nodes);
		}
		return nodes;
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
		while (p1.prev != null) {
			Node prevNode = nodes[p1.prev.node.id];
			for (Node.Edge e : prevNode.edges) {
				if (e.endPoint == p1.node.id) {
					delta = Math.min(delta, e.cost - e.flow);
				}
			}
			p1 = p1.prev;
		}
		while (path.prev != null) {
			Node prevNode = nodes[path.prev.node.id];
			for (Node.Edge e : prevNode.edges) {
				if (e.endPoint == path.node.id) { // If current edge connects
													// along this path, increase
													// it.
					e.flow = e.flow + delta;
					for (Node.Edge backwardsEdge : nodes[path.node.id].edges) { // Find
																				// backwards
																				// path
																				// and
																				// decrease
																				// by
																				// same
																				// amount.
						if (backwardsEdge.endPoint == e.startPoint) {
							backwardsEdge.flow = backwardsEdge.flow - delta;
							break;
						}
					}
					break;
				}
			}
			path = path.prev;
		}
	}

	/*
	 * private static int findDelta(Path path) { while (path.prev != null) {
	 * path = path.prev; } }
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
			for (Node.Edge edge : currentPath.node.edges) {
				Node currentNode = nodes[edge.endPoint];
				if (!visited.contains(currentNode.id) && edge.flow < edge.cost) {
					Path newPath = new Path(currentNode, currentPath);
					visited.add(currentNode.id);
					if (edge.endPoint == v) {
						return newPath; // Found the path
					} else {
						q.push(newPath); // DFS
					}
				}

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
		Node node;

		Path(Node n, Path prev) {
			this.node = n;
			this.prev = prev;
		}

		public boolean isCycle() {
			if (prev == null) {
				return false;
			} else {
				return prev.isCycleRecursive(node.id);
			}
		}

		private boolean isCycleRecursive(int id) {
			if (id == this.node.id) {
				return true;
			}
			if (prev == null) {
				return false;
			}
			return prev.isCycleRecursive(id);

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
				return startPoint + " <- " + cost + "|" + flow + " -> " + endPoint;

			}
		}
	}

}
