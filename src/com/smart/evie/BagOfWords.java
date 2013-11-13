package com.smart.evie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

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
	private static final String REGEX_WHITESPACE = "\\s+";

	/** Hashtable that maps words to its index **/
	private Hashtable<String, Integer> wordIndexMap;
	
	/** List of all word pollings across events **/
	private ArrayList<double[]> allPolls;
	
	/** The events instance that is used for allPolls **/
	private final DynamicEventList events;
	
	/** List of trivial words we don't want to include in classification **/
	private Set<String> trivialWords = new HashSet<String>
							(Arrays.asList("the", "a", "of", "is", "in", "an", "and", "or"));
	
	public BagOfWords(DynamicEventList events) {
		int wordCount = 0;
		this.wordIndexMap = new Hashtable<String, Integer>();
		this.events = events;

		if (events == null) {
			return;
		}
		
		/* Count words and create mappings of words to indices */
		for (Event event: events.getEvents()) {
			/* Remove all punctuation, lowercase, and tokenize on whitespace */
			String[] description = sanitizeDescription(event);
			
			/* Map each word to indices */
			for (String word: description) {

				word = removeFormatting(word);
				
				if ( inTrivialWordList(word) ) {
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

	private String[] sanitizeDescription(Event event) {
		String allWords = event.getDescription().concat(" " + event.getName()).concat(" " + event.getName());
		String[] description = allWords.
				replaceAll("[^a-zA-Z ]", " ").split(REGEX_WHITESPACE);
		return description;
	}
	
	/**
	 * Removes all formatting of the word - current includes removal of 
	 * "ing" and "ed", and converts to all lowercase.
	 * @param word
	 * @return
	 */
	private String removeFormatting(String word) {
		word = word.toLowerCase();
		
		String endingPresent = "ing";
		String endingPast = "ed";
		
		if (word.length() >= endingPast.length()) {
			word = removeSuffix(word, endingPast);
		} else if (word.length() >= endingPresent.length()) {
			word = removeSuffix(word, endingPresent);
		}

		return word;
	}
	
	/**
	 * invariant: suffix must have size less than or equal to size of word
	 * @param word
	 * @param suffix
	 * @return
	 */
	private String removeSuffix(String word, String suffix) {
		int suffixOffset = word.length() - suffix.length();

		if ( word.substring(suffixOffset).equals(suffix) ) {
			word = word.substring(0, suffixOffset + 1);
		}
		
		return word;
	}
	
	private boolean inTrivialWordList(String word) {
		if (trivialWords.contains(word)) {
			return true;
		}
		return false;
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
			String[] description = sanitizeDescription(event);
			
			for (String word: description) {
				word = removeFormatting(word);
				
				if ( inTrivialWordList(word) ) {
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
		
		for (String word: words.split(REGEX_WHITESPACE)) {
			/* CURRENTLY IGNORES NEW WORDS - UPDATE THIS!!! */
			if (this.wordIndexMap.containsKey(word)) { 
				featureVector[this.wordIndexMap.get(word)]++;
			}
		}

		return featureVector;
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
