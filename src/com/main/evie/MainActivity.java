package com.main.evie;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.evie.R;

public class MainActivity extends Activity {
	public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    private static final String URL = "http://teudu.andrew.cmu.edu/events.xml";
   
    // Whether there is a Wi-Fi connection.
    private static boolean wifiConnected = false; 
    // Whether there is a mobile connection.
    private static boolean mobileConnected = true; 
    public static String sPref = null;

	private static DynamicEventList dynamicEvents;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/* Check for first-time users: tell them to complete profile if first time */
		SharedPreferences userData = getSharedPreferences(getString(R.string.prefs_file_name), 0);
		if (!userData.contains(getString(R.string.nameKey))) {
			goToSignUp();
		}

		/* Triggers ping of location based services */
		//this.dynamicEvents = new DynamicEventList();
		// dynamicEvents.initiateDummyEvents(); // REMOVE THIS - FOR MOCKS ONLY
		loadEvents();

		TextView headerText = (TextView) this.findViewById(R.id.tv_header);
		headerText.setText("Hello " + userData.getString(getString(R.string.nameKey), "world") + "! You are in Gates 4307.");

		LocationUpdates listener = new LocationUpdates(headerText);
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
		
		ToggleButton freeFoodToggle = (ToggleButton) this.findViewById(R.id.tb_free_food);
		freeFoodToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton button, boolean on) {
				if (on) {
					MainActivity.dynamicEvents.filterFreeFood();
				} else {
					MainActivity.dynamicEvents.removeFilters();
				}
				updateList();
			}
		});
		
		updateList();
	}	
	
    // Uses AsyncTask to download the events XML from Teudu
    public void loadEvents() {
        if((wifiConnected || mobileConnected)) {
            //new DownloadEventsXmlTask().execute(URL);
        	try {
	            dynamicEvents = new DownloadEventsXmlTask().loadXmlFromNetwork(URL);
	        } catch (IOException e) {
	            return;
	        } catch (XmlPullParserException e) {
	            return;
	        }
        }
    }

	private void updateList() {
		EventAdapter adapter = new EventAdapter(this, R.layout.event_grid_item, dynamicEvents.getEvents());
		GridView gridView = (GridView) this.findViewById(R.id.gv_main);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new EventDetailClickListener(this, this.dynamicEvents));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void goToSignUp() {
		Intent signupIntent = new Intent(this, SignUpActivity.class);
		startActivity(signupIntent);
	}
	
	private static class EventDetailClickListener implements OnItemClickListener {

		private final Context context;
		private final DynamicEventList dynamicEvents;
		
		EventDetailClickListener(Context context, DynamicEventList dynamicEvents) {
			this.context = context;
			this.dynamicEvents = dynamicEvents;
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

	private static class LocationUpdates implements LocationListener {

		private int counter;
		private TextView location;
		
		LocationUpdates(TextView view) {
			this.location = view;
			this.counter = 0;
		}
		
		@Override
		public void onLocationChanged(Location arg0) {
		//	this.location.setText("altitude: " + arg0.getAltitude() + "longitude, latitude:" + arg0.getLongitude() + ", " + arg0.getLatitude() + 
			//			" and this is my " + counter + "th time querying");
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
	

	private class DownloadEventsXmlTask extends AsyncTask<String, Void, DynamicEventList> {
		@Override
	    protected DynamicEventList doInBackground(String... urls) {
	        try {
	            return loadXmlFromNetwork(urls[0]);
	        } catch (IOException e) {
	            return null;
	        } catch (XmlPullParserException e) {
	            return null;
	        }
	    }
		
		@Override
	    protected void onPostExecute(DynamicEventList result) {  
	        dynamicEvents = result;
	    }
		    
		// Uploads XML from teudu, parses it, and combines it with
		// HTML markup. Returns HTML string.
		private DynamicEventList loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
		    InputStream stream = null;
		    // Instantiate the parser
		    TeuduEventParser teuduEventParser = new TeuduEventParser();
		    DynamicEventList events = null;
		    String title = null;
		    String url = null;
		    String summary = null; 
		         	         	         
		    try {
		        stream = downloadUrl(urlString);        
		        events = teuduEventParser.parse(stream);
		    // Makes sure that the InputStream is closed after the app is
		    // finished using it.
		    } finally {
		        if (stream != null) {
		            stream.close();
		        } 
		        return events;
		    }
		}
		
		// Given a string representation of a URL, sets up a connection and gets
		// an input stream.
		private InputStream downloadUrl(String urlString) throws IOException {
			URL url = new URL(urlString);
		    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		    conn.setReadTimeout(10000 /* milliseconds */);
		    conn.setConnectTimeout(15000 /* milliseconds */);
		    conn.setRequestMethod("GET");
		    conn.setDoInput(true);
		    // Starts the query
		    conn.connect();
		    return conn.getInputStream();
		}
	}
}
