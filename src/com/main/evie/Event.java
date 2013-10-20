package com.main.evie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.example.evie.R;

/**
 * Contains data from event.
 *
 */
public class Event {
	private int id;
	private final String name;
	private final String description;
	private final Date startTime;
	private final Date endTime;
	private final String location;
	private final String imgUrl;
	private final ArrayList<String> categories;
	private final boolean cancelled;
	
	Event(int id, String name, String description, Date startTime, Date endTime, 
			String location, String imgUrl, String categories,
			boolean cancelled) {
		this.id = id;
		this.name = name;
		this.description = description;
		
		// Do error checking on start/end time later
		this.startTime = startTime;
		this.endTime = endTime;
		
		// Do location checking
		this.location = location;
		
		// Do url validation
		this.imgUrl = imgUrl;
		
		// Do category validation
		if (categories != null) {
			this.categories = new ArrayList<String>(Arrays.asList(categories.split(",")));
		} else {
			this.categories = null;
		}
		this.cancelled = cancelled;
	}

	/* TEMP Function to use android launcher icon as placeholder */
	public int getImageResource() {
		return R.drawable.ic_launcher;
	}
	
	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public Date getStartTime() {
		return this.startTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}
	
	public String getLocation() {
		return this.location;
	}
	
	public String getImgUrl() {
		return this.imgUrl;
	}
	
	public ArrayList<String> getCategories() {
		return this.categories;
	}
	
	public boolean isCancelled() {
		return this.cancelled;
	}
}
