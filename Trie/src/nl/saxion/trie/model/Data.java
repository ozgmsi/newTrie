package nl.saxion.trie.model;

import java.util.ArrayList;

/**
 * De data dat wordt gebruikt bij het invoegen van een string
 * @author Administrator
 *
 */
public class Data {
	
	/* De posities van de input wordt in deze lijst bijgehouden */
	private ArrayList<Integer> data;
	
	public Data(ArrayList<Integer> posities) {
		data = new ArrayList<Integer>();
		data.addAll(posities);
	}

	public ArrayList<Integer> getPositions() {
		return this.data;
	}
	
}
