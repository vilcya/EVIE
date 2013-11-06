package com.main.evie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.evie.R;
import com.smart.evie.BagOfWords;
import com.wifi.evie.WifiScanClickListener;

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
	private WifiScanClickListener scanListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setupStartPageFeatures();
		setupSmartSystem();
	}

	/**
	 * Setups what the user sees on launch
	 */
	private void setupStartPageFeatures() {
		/* Check for first-time users: tell them to complete profile if first time */
		SharedPreferences userData = getSharedPreferences(getString(R.string.prefs_file_name), 0);
		if (!userData.contains(getString(R.string.nameKey))) {
			goToSignUp();
		}
		
		/* Setup welcome text */
		TextView headerText = (TextView) this.findViewById(R.id.tv_header);
		headerText.setText("Hello " + userData.getString(getString(R.string.nameKey), "world") + "! You are in Gates 4307.");

		loadEvents();
		
		/* Setup location scan button */
		this.scanListener = new WifiScanClickListener(this);
		Button scanLocationButton = (Button) this.findViewById(R.id.b_rescan);
		scanLocationButton.setOnClickListener(this.scanListener);
		this.scanListener.registerReceiver();

		/* Setup free food toggle */
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
	
	/**
	 * Sets up all systems that have smartness
	 * Currently: 
	 * 		1. Triggers k-means training if it is needed 
	 */
	private void setupSmartSystem() {
		String filename = this.getResources().getString(R.string.filename_trained_data);
		File file = this.getApplicationContext().getFileStreamPath(filename); 

		if (file.exists()) {
			/* We've already trained on this data */
			return;
		}
		
		/* Loads training data into dynamicEvents */
		InputStream stream = this.getResources().openRawResource(R.raw.rawtrainingdata);
		dynamicEvents = new DownloadEventsXmlTask().loadXmlFromFile(stream);

		BagOfWords bag = new BagOfWords(dynamicEvents);
		ArrayList<double[]> results = bag.pollWords();
		
		/* TODO: Insert call to KMeans here using results, call bagOfWords with parameters as 
		 * 		results from KMeans, and file information */
	}
	
    // Uses AsyncTask to download the events XML from Teudu
    private void loadEvents() {
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

		UpdateEventsCallback eventCallback = new UpdateEventsCallback(this, dynamicEvents);
		eventCallback.updateList();
		Handler eventChangeHandler = new Handler(Looper.getMainLooper(), eventCallback);
		dynamicEvents.setHandler(eventChangeHandler);
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

	
	
	/* ---- EVENT DOWNLOADING ---- */
	
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

		/**
		 * Used for training
		 */
		private DynamicEventList loadXmlFromFile(InputStream stream) {
			TeuduEventParser teuduEventParser = new TeuduEventParser();
			DynamicEventList events = null;

			try {
				events = teuduEventParser.parse(stream);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return events;
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
