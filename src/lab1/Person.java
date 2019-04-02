package lab1;

import java.util.Arrays;

public class Person {

	boolean male;
	int id;
	int[] priorityList;
	int bestTried = 0;
	private Person partner;

	public Person(boolean male, int id, int[] priorityList) {
		this.id = id;
		this.male = male;
		this.priorityList = priorityList;
	}

	public void setPriorityList(int[] priorityList) {
		this.priorityList = priorityList;
	}

	@Override
	public String toString() {
		if (this.partner == null) {

			return String.format("ID: %s | male: %s| priority: %s", this.id, this.male,
					Arrays.toString(this.priorityList));
		}else {
			return String.format("ID: %s | male: %s| priority: %s | partner ID: %s", this.id, this.male,
					Arrays.toString(this.priorityList), this.partner.id);
		}
	}

	public int getBestUnTried() {
		int best = this.bestTried;
		this.bestTried += 1;
		return priorityList[best];

	}

	public Person invertPriority() {
		int[] inverted = new int[this.priorityList.length];
		int i = 1;
		for (int e : this.priorityList) {
			inverted[e - 1] = i;
			i += 1;
		}
		this.priorityList = inverted;
		return this;
	}

	public void setPartner(Person p) {
		this.partner = p;
	}

	public Person getPartner() {
		return this.partner;
	}

	public boolean hasPartner() {
		if (this.partner == null) {
			return false;
		}
		return true;
	}

	public boolean prefers(Person currentMan) {
		if (!this.hasPartner()) {
			return true;
		} else {
			int idNewMan = currentMan.id;
			int idOldMan = this.partner.id;
			return this.priorityList[idNewMan-1] < this.priorityList[idOldMan-1];

		}

	};
}
