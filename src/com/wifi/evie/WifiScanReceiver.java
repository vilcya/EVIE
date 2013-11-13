package com.wifi.evie;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.main.evie.DynamicEventList;

public class WifiScanReceiver extends BroadcastReceiver {

	private final WifiManager wifiManager;
	private static DynamicEventList dynamicEvents;
	
	public WifiScanReceiver(WifiManager wifiManager) {
		this.wifiManager = wifiManager;
		dynamicEvents = new DynamicEventList(); 
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "Scan completed", Toast.LENGTH_LONG).show();
		logScanResults(this.wifiManager.getScanResults());
		dynamicEvents.filterByLocation(this.wifiManager.getScanResults());
	}
	
	private void logScanResults(List<ScanResult> scanResults) {
		
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
	}
}
