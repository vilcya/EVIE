package com.smart.evie;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StringFormatting {
	private static final String REGEX_WHITESPACE = "\\s+";

	/** List of trivial words we don't want to include in classification **/
	private final Set<String> trivialWords = new HashSet<String>
							(Arrays.asList("the", "a", "of", "is", "in", "an", "and", "or"));
	
	public String[] sanitizeWords(String words) {
		String[] sanitizedWords = words.
				replaceAll("[^a-zA-Z ]", " ").split(REGEX_WHITESPACE);
		return sanitizedWords;
	}

	/**
	 * Removes all formatting of the word - current includes removal of 
	 * "ing" and "ed", and converts to all lowercase.
	 * @param word
	 * @return
	 */
	public String removeFormatting(String word) {
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
	
	public boolean inTrivialWordList(String word) {
		if (trivialWords.contains(word)) {
			return true;
		}
		return false;
	}
}
