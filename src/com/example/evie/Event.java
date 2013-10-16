package com.example.evie;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Contains data from event.
 * @author vilcya
 *
 */
public class Event {
	
	private AtomicInteger counter = new AtomicInteger();
	int id;
	private String name;
	private String description;
	private Date startTime;
	private Date endTime;
	
	Event(String name, String description, Date startTime, Date endTime) {
		this.id = counter.getAndIncrement();
		this.name = name;
		this.description = description;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setTime(Date startTime, Date endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

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
}
