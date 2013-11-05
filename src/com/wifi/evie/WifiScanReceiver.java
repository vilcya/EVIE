package com.wifi.evie;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.widget.GridView;
import android.widget.Toast;

import com.example.evie.R;
import com.main.evie.DynamicEventList;
import com.main.evie.EventAdapter;
import com.main.evie.MainActivity;

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
		dynamicEvents.filterByLocation(this.wifiManager.getScanResults());
	}
}
