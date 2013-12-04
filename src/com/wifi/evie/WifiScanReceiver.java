package com.wifi.evie;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evie.R;
import com.main.evie.DynamicEventList;
import com.main.evie.MainActivity;

public class WifiScanReceiver extends BroadcastReceiver {

	private final WifiManager wifiManager;
	private LocationMapping locationMapping;
	private static DynamicEventList dynamicEvents;

	public WifiScanReceiver(WifiManager wifiManager) {
		this.wifiManager = wifiManager;
		this.locationMapping = new LocationMapping();
		dynamicEvents = new DynamicEventList();
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		/** Grab the location */
		ArrayList<String> scanResults = parseScanResults(this.wifiManager.getScanResults());
		String location = this.locationMapping.getLocation(scanResults);

		logScanResults(this.wifiManager.getScanResults(), location);

		TextView headerView = (TextView) ((MainActivity)context).findViewById(R.id.tv_header);
		headerView.setText("You are near " + location + ".");
		
		/** Notify events */
		dynamicEvents.updateLocation(location, context);
	}
	
	private ArrayList<String> parseScanResults(List<ScanResult> scanResults) {
		ArrayList<String> finalResults = new ArrayList<String>();

		for (ScanResult result: scanResults) {
			finalResults.add(result.BSSID);
		}
		
		return finalResults;
	}
	
	private void logScanResults(List<ScanResult> scanResults, String location) {
		
		int index = 0;
		int maxIndex = -1;
		int maxLevel = Integer.MIN_VALUE;
		
		String allResults = "[";
		for (ScanResult result: scanResults) {
			if ( result.level > maxLevel ) {
				maxIndex = index;
				maxLevel = result.level;
			}
			allResults += result.BSSID + " / " + result.level + ", ";
			index++;
		}
		allResults += "]";

		Log.i("evie_debug", "Final list is: " + allResults);
		Log.i("evie_debug", "Largest is: " + scanResults.get(maxIndex).BSSID + " with levels (in dBm): " + maxLevel);
		Log.i("evie_debug", "I am near: " + location);
	}
}
