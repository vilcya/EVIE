package com.main.evie;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import com.example.evie.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EventAdapter extends ArrayAdapter<Event>{

	private static ArrayList<Event> events; 
	private static Context context;
	private static int resource;
	
	public EventAdapter(Context context, int resource, ArrayList<Event> events) {
		super(context, resource, events);
		this.context = context;
		this.resource = resource;
		this.events = events;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		EventViewComponents eventViews = null;
		
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) ((Activity) context).getLayoutInflater();
			row = inflater.inflate(this.resource, parent, false);
			
			eventViews = new EventViewComponents();
			eventViews.eventName = (TextView) row.findViewById(R.id.event_name);
			eventViews.eventImage = (ImageView) row.findViewById(R.id.event_icon);
			row.setTag(eventViews);
		} else {
			eventViews = (EventViewComponents) row.getTag();
		}

		Event event = this.events.get(position);
		
		eventViews.eventName.setText(event.getName());

		try {
			eventViews.eventImage.setImageBitmap(event.getImageBitmap());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return row;
	}
	
	private static class EventViewComponents {
		ImageView eventImage;
		TextView eventName;
	}
	
}
