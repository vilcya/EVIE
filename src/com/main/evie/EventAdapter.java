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
	
	private int numColors;
	private final ArrayList<Event> events; 
	private Context context;
	private int resource;
	private int count;
	
	public EventAdapter(Context context, int resource, ArrayList<Event> events) {
		super(context, resource, events);
		this.count = 0;
		this.context = context;
		this.resource = resource;
		this.events = events;
		this.numColors = this.context.getResources().getInteger(R.integer.num_colors);
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
			
			//eventViews.eventImage = (ImageView) row.findViewById(R.id.event_icon);
			row.setTag(eventViews);
			
			int color = context.getResources().obtainTypedArray(R.array.eventbg).getColor(count % numColors, 0);
			row.setBackgroundColor(color);
			count++;
		} else {
			eventViews = (EventViewComponents) row.getTag();
		}

		Event event = this.events.get(position);
		
		eventViews.eventName.setText(event.getName());

		/*try {
			eventViews.eventImage.setImageBitmap(event.getImageBitmap());
		} catch (Exception e) {
			e.printStackTrace();
		}*/

		return row;
	}
	
	private class EventViewComponents {
		ImageView eventImage;
		TextView eventName;
	}
	
}
