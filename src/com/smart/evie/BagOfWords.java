package com.smart.evie;

import java.util.ArrayList;
import java.util.Hashtable;

import android.util.Log;

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
	/** Hashtable that maps words to its index **/
	private Hashtable<String, Integer> wordIndexMap;
	
	/** List of all word pollings across events **/
	private ArrayList<double[]> allPolls;
	
	private StringFormatting format;

	public BagOfWords() {
		int wordCount = 0;
		this.wordIndexMap = new Hashtable<String, Integer>();
		this.format = new StringFormatting();
		DynamicEventList events = new DynamicEventList();
		
		if (events == null) {
			return;
		}

		/* Count words and create mappings of words to indices */
		for (Event event: events.getAllEvents()) {
			/* Remove all punctuation, lowercase, and tokenize on whitespace */
			String[] description = sanitizeEvent(event);
			
			/* Map each word to indices */
			for (String word: description) {

				word = this.format.removeFormatting(word);
				
				if ( this.format.inTrivialWordList(word) ) {
					/* We don't need to consider the word */
					continue;
				}
				
				if (!this.wordIndexMap.containsKey(word)) {
					this.wordIndexMap.put(word, wordCount);
					wordCount++;
				}
			}
		}
		//logWords();
	}
	
	private String[] sanitizeEvent(Event event) {
		String words = event.extractImportantText();
		return format.sanitizeWords(words);
	}
	

	/**
	 * Counts words in each description
	 * Eventual TODO: eliminate duplicate code in here and constructor
	 * 
	 * @return New arraylist - could be empty if no events or no descriptions 
	 */
	public ArrayList<double[]> pollWords(ArrayList<Event> events) {
		allPolls = new ArrayList<double[]>();
		
		/* Run through events and poll for each one */
		for (Event event: events) {
			
			/* Default value on initialization guaranteed to be 0 */
			double eventWordPoll[] = new double[this.wordIndexMap.size()]; 
			
			/* Tokenize on whitespace */
			String[] words = sanitizeEvent(event);
			
			for (String word: words) {
				word = format.removeFormatting(word);
				
				if ( format.inTrivialWordList(word) ) {
					/* We don't need to consider the word */
					continue;
				}
				
				eventWordPoll[this.wordIndexMap.get(word)]++;
			}

			allPolls.add(eventWordPoll);
		}

		return allPolls;
	}

	public double[] poll(String words) {
		double[] featureVector = new double[wordIndexMap.size()];
		
		for (String word: format.sanitizeWords(words)) {
			/* CURRENTLY IGNORES NEW WORDS - UPDATE THIS!!! */
			
			word = format.removeFormatting(word);

			if (!format.inTrivialWordList(word) && this.wordIndexMap.containsKey(word)) { 
				featureVector[this.wordIndexMap.get(word)]++;
			}
		}

		return featureVector;
	}
	
	public int getSize() {
		return this.wordIndexMap.size();
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
	
	/**
	 * Prints contents of bag of words into logcat
	 */
	public void log() {
		Log.i("evie_debug", "BAGOFWORDS RESULTS");
		for (double[] wordCount: this.allPolls) {
			String currentCount = "[";
			for (double count: wordCount) {
				currentCount += count + ", ";
			}
			currentCount += "]";

			Log.i("evie_debug", "wordcount " + currentCount);
		}
		
	}
	
	public void logWords() {
		for (String word : this.wordIndexMap.keySet()) {
			Log.i("evie_debug", "Word: " + word);
		}
	}
}
