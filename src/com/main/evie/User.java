package com.main.evie;
import com.example.evie.R;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;


public class User {
	
	private String mName;
	private String mAndrew;
	private String mCollege;
	private ArrayList<String> mInterests;
	private SharedPreferences mPrefs;
	private Context mContext;
	
	public User(Context c) {
		mContext = c.getApplicationContext();
		String PrefsName = mContext.getString(R.string.prefs_file_name);
		mPrefs = mContext.getSharedPreferences(PrefsName, 0);
		
		/* Fill up all the private variables with details about user */
		mName = mPrefs.getString(mContext.getString(R.string.nameKey), null);
		mAndrew = mPrefs.getString(mContext.getString(R.string.andrewKey), null);
		mCollege = mPrefs.getString(mContext.getString(R.string.collegeKey), null);
		mInterests = new ArrayList<String>();
		
		/* Sad, sad code to populate interests list */
		boolean academicInterested = mPrefs.getBoolean(mContext.getString(R.string.academicKey), false);
		boolean foodInterested = mPrefs.getBoolean(mContext.getString(R.string.foodKey), false);
		boolean culturalInterested = mPrefs.getBoolean(mContext.getString(R.string.culturalKey), false);
		boolean sportsInterested = mPrefs.getBoolean(mContext.getString(R.string.sportsKey), false);
		boolean artsInterested = mPrefs.getBoolean(mContext.getString(R.string.artsKey), false);
		boolean professionalInterested = mPrefs.getBoolean(mContext.getString(R.string.professionalKey), false);
		boolean entertainmentInterested = mPrefs.getBoolean(mContext.getString(R.string.entertainmentKey), false);
		boolean serviceInterested = mPrefs.getBoolean(mContext.getString(R.string.serviceKey), false);
		
		if (academicInterested) {
			mInterests.add(mContext.getString(R.string.academicKey));
		}
		
		if (foodInterested) {
			mInterests.add(mContext.getString(R.string.foodKey));
		}
		
		if (culturalInterested) {
			mInterests.add(mContext.getString(R.string.culturalKey));
		}
		
		if (sportsInterested) {
			mInterests.add(mContext.getString(R.string.sportsKey));
		}
		
		if (artsInterested) {
			mInterests.add(mContext.getString(R.string.artsKey));
		}
		
		if (professionalInterested) {
			mInterests.add(mContext.getString(R.string.professionalKey));
		}
		
		if (entertainmentInterested) {
			mInterests.add(mContext.getString(R.string.entertainmentKey));
		}
		
		if (serviceInterested) {
			mInterests.add(mContext.getString(R.string.serviceKey));
		}
		
	}
	
	public boolean hasPreferences() {
		if (mName != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public String getName() {
		return mName;
	}
	
	public String getAndrew() {
		return mAndrew;
	}
	
	public String getCollege() {
		return mCollege;
	}
	
	public ArrayList<String> getInterests() {
		return mInterests;
	}
	
	
}
