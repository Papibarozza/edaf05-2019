package lab4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lab4.DQ.Point;

public class DQ {
	private final static String TESTDATA_DIR = "C:\\Users\\Arvid Mildner\\Documents\\edaf05-workspace-2019\\edaf05\\4closestpair\\data\\secret\\";

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(TESTDATA_DIR + "0mini.in"));

		String str;
		int n = Integer.parseInt(in.readLine());
		ArrayList<Point> pX = new ArrayList<>();
		ArrayList<Point> pY = new ArrayList<>();
		while ((str = in.readLine()) != null) {
			try {
				String[] xy = str.split("\\s+");
				Point p = new Point(mapToInt(xy));
				pX.add(p);
				pY.add(p);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		pX.sort((a, b) -> a.x < b.x ? -1 : a.x == b.x ? 0 : 1); // Sorts array from left to right on x-coord
		pY.sort((a, b) -> a.y < b.y ? -1 : a.y == b.y ? 0 : 1); // Sorts array from left to right on y-coord

		for (Point p : pY) {
			System.out.println(p);
		}

		System.out.println(findMin(pX));
	}

	private static double findMin(ArrayList<Point> coords) {
		int xm = coords.size() / 2;
		ArrayList<Point> leftX = (ArrayList<Point>) coords.subList(0, xm+1);
		ArrayList<Point> rightX = (ArrayList<Point>) coords.subList(xm + 1, coords.size());
		
		double delta = Math.min(findMin(leftX), findMin(rightX));
		int startIdxStrip = xm-(int) Math.ceil(delta);
		int endIdxStrip = xm+(int) Math.ceil(delta);
		
		ArrayList<Point> strip = (ArrayList<Point>) coords.subList(startIdxStrip, endIdxStrip);
		strip.sort((a, b) -> a.y < b.y ? -1 : a.y == b.y ? 0 : 1); //Sorts split on y-coord
		
		return 0.0;
	}

	/**
	 * Returns a new subarray containing the elements from start -> end from input
	 * array. Ex. subArr([1,2,3,4],1,2) => [2,3]
	 * 
	 */
	public static Object[] subArray(Object[] arr, int start, int end) {
		Object[] subArr = new Object[end - start];
		for (int i = 0; i < subArr.length; i++) {
			subArr[i] = arr[end++];
		}
		return subArr;
	}

	/**
	 * Returns a new subarray containing the elements from start -> end from input
	 * array. Ex. subArr([1,2,3,4],1,2) => [2,3]
	 * 
	 */
	public static int[] subArray(int[] arr, int start, int end) {
		int[] subArr = new int[end + 1 - start];
		for (int i = 0; i < subArr.length; i++) {
			subArr[i] = arr[start++];

		}
		return subArr;
	}

	private static int[] mapToInt(String[] xy) {
		int[] intArr = new int[xy.length];
		int i = 0;
		for (String s : xy) {
			intArr[i++] = Integer.parseInt(s);
		}
		return intArr;
	}

	public static class Point {
		final int x;
		final int y;

		Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		Point(int[] arr) {
			this.x = arr[0];
			this.y = arr[1];
		}

		@Override
		public String toString() {
			return "x: " + this.x + " y: " + this.y;
		}
	}

}
