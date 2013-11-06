package com.smart.evie;

import java.util.Hashtable;

public class BagOfWords {
	private static final int CATEGORY_SIZE = 9;

	private static Hashtable<String, Double> bagOfWords[] = null;
	
	/**
	 * Initializes structures inside BagOfWords
	 */
	public void initializeBag() {
		this.bagOfWords = new Hashtable[CATEGORY_SIZE];

		for (int category = 0; category < CATEGORY_SIZE; category++) {
			this.bagOfWords[category] = new Hashtable<String, Double>();
		}
	}

	private boolean addWord(String word, int category) {
		if (this.bagOfWords == null) {
			initializeBag();
		}
		
		if (!categoryValidated(category)) {
			return false;
		}

		double probability = 0.5; // INSERT PROBABILITY CALCULATION HERE
		
		this.bagOfWords[category].put(word, probability);
		return true;
	}
	
	
	private boolean categoryValidated(int category) {
		return category > 0 && category < CATEGORY_SIZE;
	}
}
