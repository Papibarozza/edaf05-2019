package lab3;

import java.util.ArrayList;

public class Node {
	Node prev=null;
	Node next=null;
	int idx;
	int keyValue = Integer.MAX_VALUE;
	ArrayList<Edge> edgeNeighbors = new ArrayList<>();
	ArrayList<Integer> neighbors = new ArrayList<>();
	Node(int idx) {
		this.idx = idx;
	}
	
	public void addEdge(int endPoint, int weight) {
		this.edgeNeighbors.add(new Edge(endPoint,weight));
	}
	class Edge{
		int endPoint;
		int weight;
		
		Edge(int endPoint, int weight){
			this.endPoint = endPoint;
			this.weight = weight;
		}
	}
}
