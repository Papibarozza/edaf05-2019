package lab5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Gorilla {
	// private final static String TESTDATA_DIR = "C:\\Users\\Arvid
	// Mildner\\Documents\\edaf05-workspace-2019\\edaf05\\5gorilla\\data\\secret\\";
	private final static String TESTDATA_DIR = "C:/Users/Arvid/Documents/edaf05-projects/edaf05-2019/5gorilla/data/secret/";

	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(TESTDATA_DIR + "0mini.in"));
		String[] str = in.readLine().split("\\s+");
		int dims = str.length;
		int [][] opt = new int [dims][dims];
		int i=0;
		while(i<dims){
			int[] row = mapToInt(in.readLine().split("\\s+"));
			opt[i] = row;
			i++;
		}
		in.readLine(); // Skip number of queries
		ArrayList<Query> qList = new ArrayList<>();
		String queryString;
		while((queryString= in.readLine()) != null){
			qList.add(new Query(queryString.split("\\s")));
		}

		

	}
	private static class Query{
		private String s1;
		private String s2;

		Query(String [] query){
			
			this.s1 = query[0];
			this.s2 = query[1];
		}
		public String toString(){
			return s1+" "+s2;
		}
	}
	private static int[] mapToInt(String[] xy) {
		int[] intArr = new int[xy.length];
		int i = 0;
		for (String s : xy) {
			intArr[i++] = Integer.parseInt(s);
		}
		return intArr;
	}

}
