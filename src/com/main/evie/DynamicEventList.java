package com.main.evie;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.smart.evie.BagOfWords;
import com.smart.evie.KMeans;
import com.smart.evie.UserPreference;

/**
 * Contains list of events that dynamically updates based on location changes.
 *
 */
public class DynamicEventList{
	private static final int FILTER_ALL = 0;
	private static final int FILTER_RECOMMENDED = 1;
	private static final int FILTER_NEARBY = 2;
	private static final int FILTER_FOOD = 3;
	private static int currentFilter = FILTER_NEARBY;
	
	/** Includes all events */
	private static ArrayList<Event> allEvents = new ArrayList<Event>();
	/** Events that the user sees at a given time */
	private static ArrayList<Event> recommendedEvents = new ArrayList<Event>();
	private static ArrayList<Event> nearbyEvents = new ArrayList<Event>();
	private static ArrayList<Event> filteredEvents = new ArrayList<Event>();
	private static UserPreference userPreference = new UserPreference();
	private static Handler eventChangeHandler = null;
	private static String location = "";
	
	public void setHandler(Handler eventChangeHandler) {
		DynamicEventList.eventChangeHandler = eventChangeHandler;
	}

	
	public void createEvent(int id, String name, String description, Date startTime, Date endTime, 
					String location, String imgUrl, String categories, boolean cancelled) {
		Event newEvent = new Event(id, name, description, startTime, endTime, location, imgUrl, categories, cancelled);
		new ImageDownloader(newEvent);
		id++;
		DynamicEventList.allEvents.add(newEvent);
	}

	/**
	 * Returns event at the given position
	 * 
	 * @param position
	 * @return Event at the given position
	 */
	public Event getEventAt(int position) throws ArrayIndexOutOfBoundsException {
		return DynamicEventList.filteredEvents.get(position);
	}
	
	public ArrayList<Event> getEvents() {
		return DynamicEventList.filteredEvents;
	}
	
	public ArrayList<Event> getAllEvents() {
		return DynamicEventList.allEvents;
	}

	public void filter(int position, Context context) {
		switch (position) {
		case FILTER_ALL:
			removeFilters();
			currentFilter = FILTER_ALL;
			break;
		case FILTER_RECOMMENDED:
			filterByRecommended();
			currentFilter = FILTER_RECOMMENDED;
			break;
		case FILTER_NEARBY:
			filterByLocation(context);
			currentFilter = FILTER_NEARBY;
			break;
		case FILTER_FOOD:
			filterFreeFood();
			currentFilter = FILTER_FOOD;
			break;
		default:
			Log.w("evie_debug", "WARNING: INDEX OUT OF BOUNDS");
		};

		sendChangeEventMessage();
	}

	public void filterFreeFood() {
		DynamicEventList.filteredEvents.clear();
		for (Event event: DynamicEventList.allEvents) {
			if (event.getDescription().toLowerCase(Locale.getDefault()).contains("food")) {
				DynamicEventList.filteredEvents.add(event);
			}
		}
	}
	
	public void updateLocation(String location, Context context) {
		DynamicEventList.location = location.toLowerCase();
		
		if (currentFilter == FILTER_NEARBY) {
			filterByLocation(context);
			sendChangeEventMessage();
		}
	}
	
	public void filterByLocation(Context context) {
		DynamicEventList.filteredEvents.clear();
		for (Event event:DynamicEventList.allEvents) {
			if (event.getLocation().toLowerCase().contains(DynamicEventList.location)) {
				DynamicEventList.filteredEvents.add(event);
			}
		}
		
		NotifyLocationEvents notification = new NotifyLocationEvents(context);
		notification.notifyEvents(DynamicEventList.filteredEvents.size());
	}

	public void filterByCategory(int label) {
		DynamicEventList.filteredEvents.clear();
		for (Event event: DynamicEventList.allEvents) {
			if (event.getLabel() == label) {
				DynamicEventList.filteredEvents.add(event);
			}
		}
	}
	
	public void filterByRecommended() {
		DynamicEventList.filteredEvents = new ArrayList<Event>(DynamicEventList.recommendedEvents);
	}
	
	public void removeFilters() {
		DynamicEventList.filteredEvents = new ArrayList<Event>(DynamicEventList.allEvents);
	}

	public void categorize(ArrayList<double[]> trainingData, BagOfWords bagOfWords) {

		for (Event event: DynamicEventList.filteredEvents) {
			double[] featureVector = bagOfWords.poll(event.getDescription());
			KMeans classifier = new KMeans();
			int category = classifier.label(trainingData, featureVector);
			event.categorize(category);
		}
		
		sendChangeEventMessage();
	}

	public void updateUserPreference(int position, int numEvents) {
		String words = DynamicEventList.filteredEvents.get(position).extractImportantText();
		this.recommendedEvents = this.userPreference.addWords(words, numEvents);
	}
	
	/**
	 * Clears all events - hack for Teudu event parser - will later 
	 * 	look for duplicates using ID
	 */
	public void clear() {
		DynamicEventList.allEvents.clear();
		DynamicEventList.filteredEvents.clear();
		sendChangeEventMessage();
	}
	
	/**
	 * Sends a message to main UI thread to update event
	 */
	private void sendChangeEventMessage() {
		Message message = Message.obtain();
		message.what = 0;
		eventChangeHandler.sendMessage(message);
	}
}
