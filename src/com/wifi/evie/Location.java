package com.wifi.evie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import android.util.Log;

public class Location {
	private String name;
	private Set<String> macIds;
	
	Location (String name, String[] macIds) {
		this.name = name;
		this.macIds = new HashSet<String>(Arrays.asList(macIds));
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<String> getMacIds() {
		return new ArrayList<String>(this.macIds);
	}

	public void addMacId(String macId) {
		if (this.macIds != null) {
			this.macIds.add(macId);
		}
	}
	
	/**
	 * Constant time lookup
	 * @param userMacId
	 * @return
	 */
	public boolean macIdExists(String userMacId) {
		//Log.i("evie_debug", name + "NAME IS " + userMacId);
		for (String macId : macIds) {
			//Log.i("evie_debug", name + "NAME OF EXISTING IS " + macId);
			if (macId.equals(userMacId)) {
				Log.i("evie_debug", name + "NAME OF EXISTING IS " + macId + "NAME of USER is " + userMacId);
				return true;
			}
		}
		Log.i("evie_debug", "DONE\n\n");
		return this.macIds.contains(name);
	}
}
