package com.main.evie;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.evie.R;


public class EventDetailClickListener implements OnItemClickListener {

	private final Context context;
	EventDetailClickListener(Context context, DynamicEventList dynamicEvents) {
		this.context = context;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		goToEventDetails(position);
	}
	
	private void goToEventDetails(int eventPosition) {
		Intent eventDetailsIntent = new Intent(context, EventDetails.class);
		eventDetailsIntent.putExtra(context.getString(R.string.event_position), eventPosition);
		context.startActivity(eventDetailsIntent);
	}
}