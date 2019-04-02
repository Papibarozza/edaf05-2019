package lab1;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class GS {
	private final static String TESTDATA_DIR = "C:\\Users\\Arvid Mildner\\Documents\\edaf05-workspace-2019\\edaf05\\1stablemarriage\\data\\secret";

	public static void main(String[] args) {
		// String testFile = TESTDATA_DIR + "\\2testmid.in";

		Object[] persons = parseFile(System.in);
		Queue<Person> men = (LinkedList<Person>) persons[0];
		Person[] women = (Person[]) persons[1];

		while (!(men.isEmpty())) {
			Person currentMan = men.poll();
			// System.out.println(currentMan);
			int best = currentMan.getBestUnTried();
			int womanIndex = best - 1;
			Person currentWoman;
			currentWoman = women[womanIndex];

			if (!(currentWoman.hasPartner())) {
				currentMan.setPartner(currentWoman);
				currentWoman.setPartner(currentMan);
			} else if (currentWoman.prefers(currentMan)) {
				Person dumpedMan = currentWoman.getPartner();
				if (dumpedMan != null) {
					men.add(dumpedMan);
				}
				currentWoman.setPartner(currentMan);
				currentMan.setPartner(currentWoman);
			} else {
				men.add(currentMan);
			}
		}
		for (Person wom : women) {
			System.out.println(wom.getPartner().id);
		}

	}

	static Object[] parseFile(InputStream inStream) {

		Scanner sc = new Scanner(inStream);
		int n = sc.nextInt();
		Queue<Person> men = new LinkedList<>();
		Person[] women = new Person[n];
		int linenumber = 1;
		int[] idStash = new int[2 * n];
		while (linenumber <= 2 * n) {
			int id = sc.nextInt();
			int[] priorityList = new int[n];
			int i = 0;
			while (i < n) {
				priorityList[i] = sc.nextInt();
				i++;
			}
			if (idStash[id - 1] == id) {
				men.add(new Person(true, id, priorityList));
			} else {
				women[id - 1] = new Person(false, id, priorityList).invertPriority();
				idStash[id - 1] = id;
			}
			linenumber++;

		}

		Object[] returnArr = new Object[] { men, women };
		return returnArr;

	}

}
