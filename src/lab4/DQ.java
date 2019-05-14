package lab4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lab4.DQ.Point;

public class DQ {
	// private final static String TESTDATA_DIR = "C:\\Users\\Arvid
	// Mildner\\Documents\\edaf05-workspace-2019\\edaf05\\4closestpair\\data\\secret\\";
	private final static String TESTDATA_DIR = "C:/Users/Arvid/Documents/edaf05-projects/edaf05-2019/4closestpair/data/secret/";

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(TESTDATA_DIR + "5huge.in"));

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
		pX.sort((a, b) -> a.x < b.x ? -1 : a.x == b.x ? 0 : 1); // Sorts on
																// x-coord
		pY.sort((a, b) -> a.y < b.y ? -1 : a.y == b.y ? 0 : 1); // Sorts on
																// y-coord
		/*
		 * for (Point p : pX) { System.out.println(p); }
		 */
		System.out.println(findMin(pX));
		System.out.println(bruteForceClosestPair(pX));

	}

	private static double findMin(List<Point> leftX2) {
		int middleIdx = leftX2.size() / 2;
		List<Point> leftX = leftX2.subList(0, middleIdx + 1);
		List<Point> rightX = leftX2.subList(middleIdx + 1, leftX2.size());
		double delta;
		if (leftX2.size() < 5) {
			delta = bruteForceClosestPair(leftX2);
		} else {
			delta = Math.min(findMin(leftX), findMin(rightX));
		}

		List<Point> strip = extractStrip1d(leftX2, middleIdx, delta);
		strip.sort((a, b) -> a.y < b.y ? -1 : a.y == b.y ? 0 : 1); // Sorts on
																	// y-coords
		double interSetDist = findInterSetDist(strip);

		return Math.min(delta, interSetDist);
	}

	private static double bruteForceClosestPair(List<Point> strip) {
		int i = 0;
		double minDist = Double.MAX_VALUE;
		for (Point p : strip) {
			for (int j = i + 1; j < strip.size() - 1; j++) {
				double dist = dist(p, strip.get(j));
				if (dist < minDist) {
					minDist = dist;
				}
			}
			i++;

		}
		return minDist;
	}

	private static double findInterSetDist(List<Point> strip) {
		int i = 0;
		double minDist = Double.MAX_VALUE;
		for (Point p : strip) {
			for (int j = 1; j < 8 && i + j < strip.size(); j++) {
				double dist = dist(p, strip.get(i + j));
				if (dist < minDist) {
					minDist = dist;
				}
			}
			i++;

		}
		return minDist;
	}

	/**
	 * Euclidian distance between points.
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	private static double dist(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
	}

	public static List<Point> extractStrip1d(List<Point> leftX2, int idx, double delta) {
		Point middlePoint = leftX2.get(idx);
		int leftOffset = 0;
		int rightOffset = 0;
		for (int i = idx - 1; i > -1; i--) {
			Point currentPoint = leftX2.get(i);
			if (Math.abs(middlePoint.x - currentPoint.x) < delta) {
				leftOffset++;
			}
		}
		for (int i = idx + 1; i < leftX2.size(); i++) {
			Point currentPoint = leftX2.get(i);
			if (Math.abs(middlePoint.x - currentPoint.x) < delta) {
				rightOffset++;
			}
		}
		return leftX2.subList(idx - leftOffset, idx + rightOffset + 1);

	}

	/**
	 * Inclusive on index
	 */
	public static List<Point> extractStripLeft(ArrayList<Point> list, int idx, double delta) {
		Point middlePoint = list.get(idx);
		int leftOffset = 0;
		for (int i = idx - 1; i > -1; i--) {
			Point currentPoint = list.get(i);
			if (Math.abs(middlePoint.x - currentPoint.x) < delta) {
				leftOffset++;
			}
		}
		return list.subList(idx - leftOffset, idx + 1);

	}

	/**
	 * Exclusive on idx
	 */
	public static List<Point> extractStripRight(ArrayList<Point> list, int idx, double delta) {
		Point middlePoint = list.get(idx);
		int rightOffset = 0;
		for (int i = idx + 1; i < list.size(); i++) {
			Point currentPoint = list.get(i);
			if (Math.abs(middlePoint.x - currentPoint.x) < delta) {
				rightOffset++;
			}
		}
		return list.subList(idx + 1, idx + rightOffset + 1);

	}

	/**
	 * Returns a new subarray containing the elements from start -> end from
	 * input array. Ex. subArr([1,2,3,4],1,2) => [2,3]
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
	 * Returns a new subarray containing the elements from start -> end from
	 * input array. Ex. subArr([1,2,3,4],1,2) => [2,3]
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
