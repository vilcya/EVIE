package com.main.evie;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.GridView;
import android.widget.TextView;

import com.example.evie.R;

public class UpdateEventsCallback implements Handler.Callback {
	
	private final Context context;
	private DynamicEventList dynamicEvents;
	
	public UpdateEventsCallback(Context context, DynamicEventList events) {
		this.context = context;
		this.dynamicEvents = events;
	}

	/**
	 * Updates the adapter for the new filtered items
	 */
	protected void updateList() {
		EventGridAdapter adapter = new EventGridAdapter(this.context, R.layout.event_grid_item, this.dynamicEvents.getEvents());
		GridView gridView = (GridView) ((MainActivity)this.context).findViewById(R.id.gv_main);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new EventDetailClickListener(this.context));		
	}
	
	protected void updateText(int events, int scheduledEvents) {
		TextView subHeader = (TextView) ((MainActivity) this.context).findViewById(R.id.tv_header2);
		if (scheduledEvents < 20) {
			subHeader.setText("You only have " + scheduledEvents + 
						" events in your calendar! Go find some friends at the " + events + " events we recommended for you!" );
		} else {
			subHeader.setText("You're pretty busy with " + scheduledEvents + 
						" events in your calendar ... we'll just recommend " + events + " events for you." );
		}
	}

	@Override
	public boolean handleMessage(Message message) {
		int updateListMessage = ((MainActivity)context).getResources().getInteger(R.integer.message_update_events);
		int updateBusyMessage = ((MainActivity)context).getResources().getInteger(R.integer.message_update_busy);

		if (message.what == updateListMessage) {
			updateList();
			return true;
		} else if (message.what == updateBusyMessage) {
			updateText(message.arg1, message.arg2);
			return true;
		}
		return false;
	}
	
}