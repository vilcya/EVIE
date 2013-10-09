package com.example.evie;

import java.util.ArrayList;
import java.util.Date;

public class Event {
	public final int id;
	public final String name;
	public final String description;
	public final String summary;
	public final Date starttime;
	public final Date endtime;
	public final String location;
	public final String imgUrl;
	public final ArrayList<String> categories;
	public final boolean cancelled;
	
	public Event(int id, String name, String description,
			String summary, String starttime, String endtime,
			String location, String imgUrl,
			String categories, boolean cancelled){
		this.id = id;
		this.name = name;
		this.description = description;
		this.summary = summary;
		this.location = location;
		this.imgUrl = imgUrl;
		this.cancelled = cancelled;
		this.starttime = new Date();
		this.endtime = new Date();
		this.categories = new ArrayList<String>();
		
		/* Need to add code to change categories into ArrayList,
		 * times into Date objects */
	}
}
