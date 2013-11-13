package com.main.evie;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.GridView;

import com.example.evie.R;

public class UpdateEventsCallback implements Handler.Callback {
	
	private final Context context;
	private int updateListMessage;
	private DynamicEventList dynamicEvents;
	
	public UpdateEventsCallback(Context context, DynamicEventList events) {
		this.context = context;
		this.dynamicEvents = events;
		this.updateListMessage = ((MainActivity)context).getResources().getInteger(R.integer.message_update_events);
	}

	/**
	 * Updates the adapter for the new filtered items
	 */
	protected void updateList() {
		EventAdapter adapter = new EventAdapter(this.context, R.layout.event_grid_item, this.dynamicEvents.getEvents());
		GridView gridView = (GridView) ((MainActivity)this.context).findViewById(R.id.gv_main);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new EventDetailClickListener(this.context, this.dynamicEvents));
	}

	@Override
	public boolean handleMessage(Message message) {
		if (message.what == this.updateListMessage) {
			updateList();
			return true;
		}
		return false;
	}
	
}