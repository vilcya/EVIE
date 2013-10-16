package com.example.evie;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class EventDetails extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_details);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_details, menu);
		return true;
	}
}
