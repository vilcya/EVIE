package com.main.evie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.evie.R;

import android.content.res.Resources;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.Message;

/**
 * Contains list of events that dynamically updates based on location changes.
 *
 */
public class DynamicEventList{

	/** Includes all events */
	private static ArrayList<Event> allEvents = new ArrayList<Event>();
	/** Events that the user sees at a given time */
	private static ArrayList<Event> filteredEvents = new ArrayList<Event>();
	private static Handler eventChangeHandler = null;
	
	int id = 0;	// REMOVE THIS - ONLY FOR MOCK UPS

	public void setHandler(Handler eventChangeHandler) {
		DynamicEventList.eventChangeHandler = eventChangeHandler;
	}

	public void initiateDummyEvents() {
		DynamicEventList.allEvents.clear();
		DynamicEventList.filteredEvents.clear();
		createEvent("Intelligence Lab", "It's demo time!", null, null);
		createEvent("Random Tech Talk", "Come join us for free food!", null, null);
		createEvent("Spontaneous BBQ", "JOIN US FOR STEAK AND MORE FOODS!", null, null);
		createEvent("SCS Day", "We haz talents too.", null, null);
		createEvent("Puppy Stress Relief", "Puppies!", null, null);
	}

	/* THIS FUNCTION IS FOR MOCK UPS - MUST INTEGRATE WITH TEUDU PARSER */
	public void createEvent(String name, String description, Date startTime, Date endTime) {
		Event newEvent = new Event(id, name, description, startTime, endTime, null, null, null, false);
		id++;
		DynamicEventList.allEvents.add(newEvent);
		DynamicEventList.filteredEvents.add(newEvent);
	}

	/**
	 * Returns event at the given position
	 * 
	 * @param position
	 * @return Event at the given position
	 */
	public Event getEventAt(int position) throws ArrayIndexOutOfBoundsException {
		return allEvents.get(position);
	}
	
	public ArrayList<Event> getEvents() {
		return DynamicEventList.filteredEvents;
	}

	public void filterFreeFood() {
		DynamicEventList.filteredEvents.clear();
		for (Event event: DynamicEventList.allEvents) {
			if (event.getDescription().toLowerCase(Locale.getDefault()).contains("food")) {
				DynamicEventList.filteredEvents.add(event);
			}
		}
		
		sendChangeEventMessage();
	}
	
	public void filterByLocation(List<ScanResult> scanResults) {
		filterFreeFood();
		sendChangeEventMessage();
	}

	public void removeFilters() {
		DynamicEventList.filteredEvents = new ArrayList<Event>(DynamicEventList.allEvents);
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
