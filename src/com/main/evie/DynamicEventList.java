package com.main.evie;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ScrollView;

/**
 * Contains list of events that dynamically updates based on location changes.
 *
 */
public class DynamicEventList{

	/** Includes all events */
	private static ArrayList<Event> allEvents = new ArrayList<Event>();
	/** Events that the user sees at a given time */
	private static ArrayList<Event> filteredEvents = new ArrayList<Event>();

	int id = 0;	// REMOVE THIS - ONLY FOR MOCK UPS

	public void initiateDummyEvents() {
		allEvents.clear();
		createEvent("Intelligence Lab", "It's demo time!", null, null);
		createEvent("Random Tech Talk", "Come join us for free food!", null, null);
		createEvent("SCS Day", "We haz talents too.", null, null);
	}

	/* THIS FUNCTION IS FOR MOCK UPS - MUST INTEGRATE WITH TEUDU PARSER */
	public void createEvent(String name, String description, Date startTime, Date endTime) {
		Event newEvent = new Event(id, name, description, startTime, endTime, null, null, null, false);
		id++;
		allEvents.add(newEvent);
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
		return allEvents;
	}
}
