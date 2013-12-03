package com.main.evie;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;

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
	private URL imgUrl;					/* May not exist */
	private final ArrayList<String> categories;
	private int category;
	private int labelledCategory;
	private final boolean cancelled;
	private Bitmap bitmap;
	private ArrayList<String> hashtags;
	
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
		try {
			this.imgUrl = new URL("http://teudu.andrew.cmu.edu" + imgUrl);
		} catch (MalformedURLException e) {
			this.imgUrl = null;
			this.bitmap = null;
			e.printStackTrace();
		}

		// Do category validation
		if (categories != null) {
			this.categories = new ArrayList<String>(Arrays.asList(categories.split(",")));
		} else {
			this.categories = null;
		}
		this.cancelled = cancelled;
	}

	/* TEMP Function to use android launcher icon as placeholder */
	public Bitmap getImageBitmap() {
		return this.bitmap;
	}
	
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
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
	
	public URL getImgUrl() {
		return this.imgUrl;
	}
	
	public ArrayList<String> getEventCategories() {
		return this.categories;
	}
	
	public int getLabel() {
		return this.category;
	}
	
	public boolean isCancelled() {
		return this.cancelled;
	}
	
	public void categorize(int category) {
		this.category = category;
	}
	
	public String extractImportantText() {
		return this.description.concat(" " + this.name);
	}
}
