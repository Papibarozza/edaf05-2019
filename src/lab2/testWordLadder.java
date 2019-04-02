package lab2;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import junit.framework.TestCase;

class testWordLadder extends TestCase{
	private final static String TESTDATA_DIR = "C:\\Users\\Arvid Mildner\\Documents\\edaf05-workspace-2019\\edaf05\\2wordladders\\data\\secret\\";


	
	@Test
	protected void setUp() {
		File input = new File(TESTDATA_DIR + "1small1.in");
		try {
			Scanner sc = new Scanner(input);
			int n = sc.nextInt();
			int q = sc.nextInt();
			String [] words = new String[n];
			int lineNumber = 0;
			sc.nextLine();
			while (lineNumber < n) {
				words[lineNumber] = sc.nextLine();
				lineNumber++;
			}

			//boolean [][] graph = WordLadder.createEdgeMatrix(words);
			String testStr1 = "baccc";
			String targetStr1 = "aaacb";
			String testStr = "cbcca";
			String targetStr = "cbbcb";
	
			//assertEquals(2, WordLadder.testQuery(testStr, targetStr, graph, words));
			//assertEquals(3, WordLadder.testQuery(testStr1, targetStr1, graph, words));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
