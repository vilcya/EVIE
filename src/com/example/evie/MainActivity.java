package com.example.evie;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.Menu;
import android.widget.GridView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static DynamicEventList dynamicEvents;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/* Triggers ping of location based services */
		this.dynamicEvents = new DynamicEventList(this);
		
		LocationUpdates listener = new LocationUpdates((TextView)this.findViewById(R.id.textView1));
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);

		updateList();
	}

	private void updateList() {
		dynamicEvents.createEvent("event1", "say hello world", null, null);
		dynamicEvents.createEvent("event2", "say hello world", null, null);
		dynamicEvents.createEvent("event3", "say hello world", null, null);
		EventAdapter adapter = new EventAdapter(this, R.layout.event_grid_item, dynamicEvents.getEvents());
		GridView gridView = (GridView) this.findViewById(R.id.main_grid_view);
		gridView.setAdapter(adapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private static class LocationUpdates implements LocationListener {

		private int counter;
		private TextView location;
		
		LocationUpdates(TextView view) {
			this.location = view;
			this.counter = 0;
		}
		
		@Override
		public void onLocationChanged(Location arg0) {
			this.location.setText("altitude: " + arg0.getAltitude() + "longitude, latitude:" + arg0.getLongitude() + ", " + arg0.getLatitude() + 
						" and this is my " + counter + "th time querying");
			this.counter++;
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
