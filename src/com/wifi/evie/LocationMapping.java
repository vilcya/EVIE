package com.wifi.evie;

import java.util.ArrayList;

import android.util.Log;

/** This class is completely hardcoded to three locations: 6th Floor Lounge, Rashid, and Tazza */
public class LocationMapping {
	
	public static final int OVERLAP_THRESHOLD = 4;
	
	/** List of locations */
	private ArrayList<Location> locations;
	private static final String[] locationNames = {"6th Floor Lounge", "Rashid Auditorium", 
													"Tazza", "Fifth and Neville", "GHC 4101"};
	
	LocationMapping() {
		this.locations = new ArrayList<Location>();
		
		Location location1 = new Location(locationNames[0], HardcodedLocations.location1);
		Location location2 = new Location(locationNames[1], HardcodedLocations.location2);
		Location location3 = new Location(locationNames[2], HardcodedLocations.location3);
		Location location4 = new Location(locationNames[3], HardcodedLocations.location4);
		Location location5 = new Location(locationNames[4], HardcodedLocations.location5);

		this.locations.add(location1);
		this.locations.add(location2);
		this.locations.add(location3);
		this.locations.add(location4);
		this.locations.add(location5);
	}
	
	public String getLocation(ArrayList<String> userMacIds) {
	
		int largestOverlap = 0;
		String location = "the middle of nowhere";
		for (int index = 0; index < locations.size(); index++) {
			int overlap = overlap(userMacIds, locations.get(index));
			if (overlap > largestOverlap) {
				largestOverlap = overlap;
				location = this.locationNames[index];
			}
		}

		return location;
	}

	private int overlap(ArrayList<String> userMacIds, Location location) {
		
		int overlap = 0;
		for (String macId : userMacIds) {
			if (location.macIdExists(macId)) {
				overlap++;
			}
		}
		
		return overlap;
	}
}
