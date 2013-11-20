package com.main.evie;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.evie.R;
import com.smart.evie.BagOfWords;
import com.smart.evie.KMeans;
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

	private static DynamicEventList dynamicEvents = new DynamicEventList();
	private WifiScanClickListener scanListener;
	
	ArrayList<double[]> trainingResults;
	BagOfWords bagOfWords;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//setupSmartSystem();
		setupStartPageFeatures();
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
		
		Availability a = new Availability(getApplicationContext());
		int numEvents = a.numEvents();
		Toast.makeText(getApplicationContext(), "Number of Events: " + Integer.toString(numEvents), Toast.LENGTH_LONG).show();
		
		/* Setup location scan button */
		/*
		this.scanListener = new WifiScanClickListener(this);
		Button scanLocationButton = (Button) this.findViewById(R.id.b_rescan);
		scanLocationButton.setOnClickListener(this.scanListener);
		this.scanListener.registerReceiver();
		*/

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
		
		Button showAllEvents = (Button) this.findViewById(R.id.b_showall);
		showAllEvents.setOnClickListener(new ShowAllEventsButtonListener());
	}
	
	/**
	 * Sets up all systems that have smartness
	 * Currently: 
	 * 		1. Triggers k-means training if it is needed 
	 */
	private void setupSmartSystem() {
    	UpdateEventsCallback eventCallback = new UpdateEventsCallback(this, dynamicEvents);
		eventCallback.updateList();
    	Handler eventChangeHandler = new Handler(Looper.getMainLooper(), eventCallback);
		dynamicEvents.setHandler(eventChangeHandler);

		Toast.makeText(getApplicationContext(), "Setting up smart system", Toast.LENGTH_LONG).show();

		String filename = this.getResources().getString(R.string.filename_trained_data);
		File file = this.getApplicationContext().getFileStreamPath(filename); 
		

		Toast.makeText(getApplicationContext(), "Got the file", Toast.LENGTH_LONG).show();

		if (file.exists()) {
			/* We've already trained on this data */
			Toast.makeText(getApplicationContext(), "We already trained", Toast.LENGTH_LONG).show();
			return;
		}
		
		Toast.makeText(getApplicationContext(), "We have not trained ... training ....", Toast.LENGTH_LONG).show();
		
		/* Loads training data into dynamicEvents */
		dynamicEvents = new DownloadEventsXmlTask().loadXmlFromFile(true, this);

		this.bagOfWords = new BagOfWords();
		ArrayList<double[]> trainingData = this.bagOfWords.pollWords(dynamicEvents.getAllEvents());

		Toast.makeText(getApplicationContext(), "TRAINING DATA: " + trainingData.toString(), Toast.LENGTH_LONG).show();
		
		KMeans kmeans = new KMeans();
		this.trainingResults = kmeans.train(trainingData);

		/* Remove this if no want results */
		//logResults();
		
		Toast.makeText(getApplicationContext(), "Training RESULTS: " + this.trainingResults, Toast.LENGTH_LONG).show();

	
	}
	
	private void logResults() {
		bagOfWords.log();
		
		Log.i("evie_debug", "TRAINING RESULTS");
		for (double[] wordCount: this.trainingResults) {
			String currentCount = "[";
			for (double count: wordCount) {
				currentCount += count + ", ";
			}
			currentCount += "]";

			Log.i("evie_debug", "wordcount " + currentCount);
		}
	}
/*
	private void populateCategorySpinner() {
		Spinner categorySpinner = (Spinner) findViewById(R.id.s_category);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.categories_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categorySpinner.setAdapter(adapter);

		categorySpinner.setOnItemSelectedListener(new CategorySortSpinnerListener());
	}*/
	
    // Uses AsyncTask to download the events XML from Teudu
    private void loadEvents() {
    	UpdateEventsCallback eventCallback = new UpdateEventsCallback(this, dynamicEvents);
		eventCallback.updateList();
    	Handler eventChangeHandler = new Handler(Looper.getMainLooper(), eventCallback);
		dynamicEvents.setHandler(eventChangeHandler);

    	/*
        if((wifiConnected || mobileConnected)) {
            //new DownloadEventsXmlTask().execute(URL);
        	try {
				dynamicEvents = new DownloadEventsXmlTask().loadXmlFromNetwork(URL);
	        } catch (IOException e) {
	            return;
	        } catch (XmlPullParserException e) {
	            return;
	        }
        }*/

    	dynamicEvents = new DownloadEventsXmlTask().loadXmlFromFile(false, this);
		//dynamicEvents.categorize(this.trainingResults, this.bagOfWords);
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
		if (this.scanListener != null) {
			this.scanListener.deregisterReceiver();
		}
		/* TODO: clean up listeners */
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (this.scanListener != null) {
			this.scanListener.registerReceiver();
		}
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
	
	private class DownloadEventsXmlTask /*extends AsyncTask<String, Void, DynamicEventList> */{
		/*@Override
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
	    }*/

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
		private DynamicEventList loadXmlFromFile(boolean training, Context context) {
			InputStream stream = null;
			if (training) {
				stream = context.getResources().openRawResource(R.raw.rawtrainingdata);
			} else {
				stream = context.getResources().openRawResource(R.raw.testdata);
			}
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
	
	private static class CategorySortSpinnerListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			MainActivity.dynamicEvents.filterByCategory(pos);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			/* Do nothing */
		}
		
	}
	
	private static class ShowAllEventsButtonListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			MainActivity.dynamicEvents.removeFilters();
		}
		
	}
}
