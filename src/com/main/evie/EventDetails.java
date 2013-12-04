package com.main.evie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.evie.R;

public class EventDetails extends Activity {

	private Event event;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_details);

		Intent myIntent = getIntent();

		int eventPosition = myIntent.getIntExtra(getString(R.string.event_position), -1);
		if (eventPosition == -1) {
			throw new IllegalArgumentException("There is no event information for event details!");
		}
	
		this.event = new DynamicEventList().getEventAt(eventPosition);
		if (this.event.getImageBitmap() == null) {
			new ImageDownloader(this.event);
		}
		
		setEventDetailsView();
		
		Availability a = new Availability(getApplicationContext());
		int numEvents = a.numEvents();

		new DynamicEventList().updateUserPreference(eventPosition, numEvents);
	}

	private void setEventDetailsView() {
		String location = "";
		String startTime = "";
		String endTime = "";
		TextView eventName = (TextView) this.findViewById(R.id.tv_event_detail_name);
		eventName.setText(this.event.getName());
		
		//TextView eventHashtags = (TextView) this.findViewById(R.id.tv_event_detail_hashtags);
		//eventHashtags.setText(this.event.getHashtags());
		
		TextView eventDescription = (TextView) this.findViewById(R.id.tv_event_detail_description);
		location = "Location: " + this.event.getLocation() + "\n";
		if (this.event.getStartTime() != null) {
			startTime = "Start Time: " + this.event.getStartTime().toString() + "\n";
		}
		if (this.event.getEndTime() != null) {
			endTime = "End Time: " + this.event.getEndTime().toString() + "\n";
		}
		eventDescription.setText(location + startTime + endTime + "\n" + this.event.getDescription());
		eventDescription.setMovementMethod(new ScrollingMovementMethod());
		
		ImageView eventImage = (ImageView) this.findViewById(R.id.iv_event_detail_image);
		Bitmap bitmap = this.event.getImageBitmap();
		if (bitmap != null) {
			eventImage.setImageBitmap(bitmap);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_details, menu);
		return true;
	}
}
