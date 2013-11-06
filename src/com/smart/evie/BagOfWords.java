package com.smart.evie;

import java.util.ArrayList;
import java.util.Hashtable;

import com.main.evie.DynamicEventList;
import com.main.evie.Event;

/**
 * Bag of Words
 * 
 * Example: 
 * 		BagOfWords bag = new BagOfWords(DynamicEventList);
 * 		ArrayList<int[]> results = bag.pollWords();
 * 
 * Later access of same results (note pollWords must have been called before):
 * 		ArrayList<int[]> results = bag.getPollResults();
 * 
 * @author 
 *
 */
public class BagOfWords {
	private static final String REGEX_WHITESPACE = "\\s+";

	/** Hashtable that maps words to its index **/
	private Hashtable<String, Integer> wordIndexMap;
	
	/** List of all word pollings across events **/
	private ArrayList<double[]> allPolls;
	
	/** The events instance that is used for allPolls **/
	private final DynamicEventList events;
	
	public BagOfWords(DynamicEventList events) {
		int wordCount = 0;
		this.wordIndexMap = new Hashtable<String, Integer>();
		this.events = events;

		if (events == null) {
			return;
		}
		
		/* Count words and create mappings of words to indices */
		for (Event event: events.getEvents()) {
			/* Tokenize on whitespace */
			String[] description = event.getDescription().split(REGEX_WHITESPACE);
			
			/* Map each word to indices */
			for (String word: description) {
				if (!this.wordIndexMap.containsKey(word)) {
					this.wordIndexMap.put(word, wordCount);
					wordCount++;
				}
			}
		}
	}

	/**
	 * Counts words in each description
	 * Eventual TODO: eliminate duplicate code in here and constructor
	 * 
	 * @return New arraylist - could be empty if no events or no descriptions 
	 */
	public ArrayList<double[]> pollWords() {
		allPolls = new ArrayList<double[]>();
		
		/* Run through events and poll for each one */
		for (Event event: events.getEvents()) {
			
			/* Default value on initialization guaranteed to be 0 */
			double eventWordPoll[] = new double[this.wordIndexMap.size()]; 
			
			/* Tokenize on whitespace */
			String[] description = event.getDescription().split(REGEX_WHITESPACE);
			
			for (String word: description) {
				eventWordPoll[this.wordIndexMap.get(word)]++;
			}

			allPolls.add(eventWordPoll);
		}

		return allPolls;
	}

	public double[] poll(String words) {
		double[] featureVector = new double[wordIndexMap.size()];
		
		for (String word: words.split(REGEX_WHITESPACE)) {
			/* CURRENTLY IGNORES NEW WORDS - UPDATE THIS!!! */
			if (this.wordIndexMap.containsKey(word)) { 
				featureVector[this.wordIndexMap.get(word)]++;
			}
		}

		return null;
	}
	
	/**
	 * Retrieves polling result
	 *  
	 * @return null if pollWords has never been called
	 * 		   ArrayList<int[]> for each word poll in each event
	 */
	public ArrayList<double[]> getPollResult() {
		return this.allPolls;
	}
}
