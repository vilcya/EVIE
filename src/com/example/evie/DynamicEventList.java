package com.example.evie;

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
 * @author vilcya
 *
 */
public class DynamicEventList{

	private static ArrayList<Event> events;
	private static Context context;
	
	DynamicEventList(Context context) {
		this.events = new ArrayList<Event>();

		// REMOVE THIS LINE WHEN IN PRODUCTION
		createEvent("derp", "hello world", null, null);
	}

	public void createEvent(String name, String description, Date startTime, Date endTime) {
		Event newEvent = new Event(name, description, startTime, endTime);
		
		events.add(newEvent);
	}

	public ArrayList<Event> getEvents() {
		return events;
	}
}
