package com.main.evie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.evie.R;
import com.wifi.evie.WifiScanClickListener;

public class MainActivity extends Activity {

	private static DynamicEventList dynamicEvents;
	private WifiScanClickListener scanListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/* Check for first-time users: tell them to complete profile if first time */
		SharedPreferences userData = getSharedPreferences(getString(R.string.prefs_file_name), 0);
		if (!userData.contains(getString(R.string.nameKey))) {
			goToSignUp();
		}

		/* WILL Trigger ping of location based services  (currently using a location scan button) */
		dynamicEvents = new DynamicEventList();
		UpdateEventsCallback eventCallback = new UpdateEventsCallback(this);
		eventCallback.updateList();
		Handler eventChangeHandler = new Handler(Looper.getMainLooper(), eventCallback);
		dynamicEvents.setHandler(eventChangeHandler);
		dynamicEvents.initiateDummyEvents(); // REMOVE THIS - FOR MOCKS ONLY

		/* Setup welcome text */
		TextView headerText = (TextView) this.findViewById(R.id.tv_header);
		headerText.setText("Hello " + userData.getString(getString(R.string.nameKey), "world") + "! You are in Gates 4307.");
		
		/* Location scan button */
		this.scanListener = new WifiScanClickListener(this);
		Button scanLocationButton = (Button) this.findViewById(R.id.b_rescan);
		scanLocationButton.setOnClickListener(this.scanListener);
		this.scanListener.registerReceiver();
		
		/* Free food toggle */
		ToggleButton freeFoodToggle = (ToggleButton) this.findViewById(R.id.tb_free_food);
		freeFoodToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton button, boolean on) {
				if (on) {
					MainActivity.dynamicEvents.filterFreeFood();
				} else {
					MainActivity.dynamicEvents.removeFilters();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		this.scanListener.deregisterReceiver();
		/* TODO: clean up listeners */
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		this.scanListener.registerReceiver();
		/* TODO: re-register listeners */
	}

	/**
	 * Starts the signup intent for first time users.
	 */
	public void goToSignUp() {
		Intent signupIntent = new Intent(this, SignUpActivity.class);
		startActivity(signupIntent);
	}

	/* ---- CLICK LISTENERS ---- */

	private static class EventDetailClickListener implements OnItemClickListener {

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
	
	/* ---- CALLBACK HANDLER ---- */
	
	private static class UpdateEventsCallback implements Handler.Callback {
		
		private final Context context;
		private int updateListMessage;
		
		public UpdateEventsCallback(Context context) {
			this.context = context;
			this.updateListMessage = ((MainActivity)context).getResources().getInteger(R.integer.message_update_events);
		}

		/**
		 * Updates the adapter for the new filtered items
		 */
		protected void updateList() {
			EventAdapter adapter = new EventAdapter(this.context, R.layout.event_grid_item, dynamicEvents.getEvents());
			GridView gridView = (GridView) ((MainActivity)this.context).findViewById(R.id.gv_main);
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener(new EventDetailClickListener(this.context, dynamicEvents));
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
}
