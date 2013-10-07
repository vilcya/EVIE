package com.example.evie;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
//import android.content.res.Resources;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Button;
import android.widget.TextView;


public class SignUpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		populateSchools();
		populateInterests();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}
	
	public void submitInfo(View view) {
		SharedPreferences userData = getSharedPreferences(getString(R.string.prefs_file_name), 0);
		SharedPreferences.Editor editor = userData.edit();
		
		final EditText nameField = (EditText) findViewById(R.id.editText1);
		String name = nameField.getText().toString();
		editor.putString(getString(R.string.nameKey), name);
		
		final EditText andrewField = (EditText) findViewById(R.id.editText2);
		String andrew = andrewField.getText().toString();
		editor.putString(getString(R.string.andrewKey), andrew);
		
		final Spinner collegeSpinner = (Spinner) findViewById(R.id.spinner1);
		String college = collegeSpinner.getSelectedItem().toString();
		editor.putString(getString(R.string.collegeKey), college);
		
		final CheckBox foodBox = (CheckBox) findViewById(R.id.checkBox1);
		boolean foodChecked = foodBox.isChecked();
		editor.putBoolean(getString(R.string.foodKey), foodChecked);
		
		final CheckBox serviceBox = (CheckBox) findViewById(R.id.checkBox2);
		boolean serviceChecked = serviceBox.isChecked();
		editor.putBoolean(getString(R.string.serviceKey), serviceChecked);
		
		final CheckBox entertainmentBox = (CheckBox) findViewById(R.id.checkBox4);
		boolean entertainmentChecked = entertainmentBox.isChecked();
		editor.putBoolean(getString(R.string.entertainmentKey), entertainmentChecked);
		
		final CheckBox culturalBox = (CheckBox) findViewById(R.id.checkBox3);
		boolean culturalChecked = culturalBox.isChecked();
		editor.putBoolean(getString(R.string.culturalKey), culturalChecked);
		
		final CheckBox artsBox = (CheckBox) findViewById(R.id.checkBox6);
		boolean artsChecked = artsBox.isChecked();
		editor.putBoolean(getString(R.string.artsKey), artsChecked);
		
		final CheckBox sportsBox = (CheckBox) findViewById(R.id.checkBox7);
		boolean sportsChecked = sportsBox.isChecked();
		editor.putBoolean(getString(R.string.sportsKey), sportsChecked);
		
		final CheckBox academicBox = (CheckBox) findViewById(R.id.checkBox8);
		boolean academicChecked = academicBox.isChecked();
		editor.putBoolean(getString(R.string.academicKey), academicChecked);
		
		final CheckBox professionalBox = (CheckBox) findViewById(R.id.checkBox5);
		boolean professionalChecked = professionalBox.isChecked();
		editor.putBoolean(getString(R.string.professionalKey), professionalChecked);
		
		editor.commit();
		
		/* 
		 * Following code should be deleted once we have valid operation ready for form submission
		 * For now it just tests functionality >.<
		 */
		User user = new User(this);
		String displayPrefs = user.getName() + " " + user.getAndrew() +
				" " + user.getCollege();
		
		for (String s : user.getInterests()) {
			displayPrefs += " " + s;
		}
		
		TextView userPrefs = (TextView) findViewById(R.id.textView7);
		userPrefs.append(displayPrefs);
	}
	
	private void populateSchools() {
		/*
		 * Adds the schools to the drop down 
		 */
		Spinner schoolSpinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.schools_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		schoolSpinner.setAdapter(adapter);
	}
	
	private void populateInterests() {
		/*
		 * Makes checkboxes for all the interests and also aligns the submit button to be below
		 * them.
		 */
		/* Dead code :( Trying to dynamically populate interest checkboxes */
		/*Resources res = getResources();
		String[] interests = res.getStringArray(R.array.interests_array);
		int lastID;
		for ( int interestIndex = 0; interestIndex < interests.length; interestIndex++ ) {
			CheckBox box = new CheckBox(this);
			box.setId(generateViewId());*/
			/*<CheckBox
	        android:id="@+id/checkBox8"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:layout_alignLeft="@+id/checkBox7"
	        android:layout_below="@+id/checkBox                                                                                                           7"
	        android:text="Academic" />*/
			
			/*if (interestIndex + 1 == interests.length) {
				lastID = box.getId();
			}
		}*/
		
		Button submit = (Button) findViewById(R.id.button1);
		ViewGroup.LayoutParams buttonLayoutParams = submit.getLayoutParams();
		((RelativeLayout.LayoutParams)buttonLayoutParams).addRule(RelativeLayout.BELOW, R.id.checkBox8);
		submit.setLayoutParams(buttonLayoutParams);
	}

}
