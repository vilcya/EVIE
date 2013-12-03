package com.smart.evie;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.main.evie.Availability;
import com.main.evie.DynamicEventList;
import com.main.evie.Event;


public class UserPreference {
	private static final int RECOMMENDATIONS = 14;
	private static final int BUSY_CAP = 15;
	private static final int RECENT_WEIGHT = 3;
	private static final double DECAY = 0.8;
	private static final double EVENT_CAP_WEIGHT = -0.8;
	private static final double MIN_NUM_EVENTS_SHOWN = 5.0;

	private double means[];
	private int meanNumber;
	private BagOfWords bag;
	private VectorUtil vectorUtil;
	private DynamicEventList events;

	public UserPreference() {
		this.vectorUtil = new VectorUtil();
		this.events = new DynamicEventList();

		this.means = null;
		this.meanNumber = 0;
	}

	public ArrayList<Event> addWords(String words, int numEvents) {
		this.bag = new BagOfWords();
		ArrayList<double[]> allPollResults = this.bag.pollWords(this.events.getAllEvents());
		double poll[] = this.bag.poll(words);

		Log.i("evie_debug", "size of poll results is " + allPollResults.size());
		updateNewMeans(poll);

		PriorityQueue<Tuple> queue = new PriorityQueue<Tuple>
						(poll.length == 0? 10: poll.length, new TupleComparator());
		int index = 0;
		for (double[] pastPoll: allPollResults) {
			double similarity = this.vectorUtil.euclideanDistance(this.means, pastPoll);
			queue.add(new Tuple(similarity, index));
			index++;
		}
		
		ArrayList<Event> topRecommendations = new ArrayList<Event>();
		int event_size_cap = 0;

		if (numEvents <= RECOMMENDATIONS) {
			event_size_cap = BUSY_CAP;
		}
		else {
			event_size_cap = Math.min(queue.size(), 
				(int)Math.ceil(EVENT_CAP_WEIGHT*numEvents + (numEvents+MIN_NUM_EVENTS_SHOWN)));
		}
		
		Log.i("evie_debug", "showing" + event_size_cap + " events out of " + queue.size());

		for (int i = 0; i < event_size_cap; i++ ) {
			if (queue.isEmpty()) {
				Log.i("evie_debug", "queue is empty");
				break;
			}
			
			Tuple current = queue.remove();
			Log.i("evie_debug", "found tuple! index " + current.index + " similarity " + current.similarity);
			topRecommendations.add(this.events.getAllEvents().get(current.index));
		}

		return topRecommendations;
	}
	
	private void updateNewMeans(double[] newPoll) {
		double[] resultingMean;
		if (this.means == null) {
			this.means = new double[newPoll.length];
		}
		if (meanNumber != 0) {
			resultingMean = this.vectorUtil.multiplyVector(this.means, meanNumber*DECAY);
		}
		
		resultingMean = this.vectorUtil.sumVectors(this.means, newPoll);
		this.meanNumber++;
		resultingMean = this.vectorUtil.divideVector(resultingMean, this.meanNumber);
		this.means = resultingMean;
	}
	
	private static class TupleComparator implements Comparator<Tuple> {

		@Override
		public int compare(Tuple a, Tuple b) {
			return Double.compare(a.similarity, b.similarity);
		}
		
	}

	private static class Tuple {
		private final double similarity;
		private final int index; 

		public Tuple(double similarity, int index) {
			this.similarity = similarity;
			this.index = index;
		}
	}
}
