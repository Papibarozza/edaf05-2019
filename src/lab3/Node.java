package lab3;

import java.util.ArrayList;

public class Node {
	Node prev=null;
	Node next=null;
	int idx;
	int keyValue = Integer.MAX_VALUE;
	ArrayList<Integer> neighbors = new ArrayList<>();

	Node(int idx) {
		this.idx = idx;
	}
}
