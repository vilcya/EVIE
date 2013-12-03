package com.main.evie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
		setEventDetailsView();

		new DynamicEventList().updateUserPreference(eventPosition);
	}
	
	private void setEventDetailsView() {
		TextView eventName = (TextView) this.findViewById(R.id.tv_event_detail_name);
		eventName.setText(this.event.getName());
		TextView eventDescription = (TextView) this.findViewById(R.id.tv_event_detail_description);
		eventDescription.setText(this.event.getDescription());
		
		ImageView eventImage = (ImageView) this.findViewById(R.id.iv_event_detail_image);
		eventImage.setImageBitmap(this.event.getImageBitmap());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_details, menu);
		return true;
	}
}
