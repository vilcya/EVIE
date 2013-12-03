package com.wifi.evie;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.example.evie.R;

public class PeriodicWifiScanner implements Runnable {

	private final Context context;
	private final WifiManager wifiManager;
	private final WifiScanReceiver scanReceiver;

	public PeriodicWifiScanner (Context context) {
		this.context = context;
		this.wifiManager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
		this.scanReceiver = new WifiScanReceiver(this.wifiManager);

		setupWifi();
	}
	
	public void registerReceiver() {
		this.context.registerReceiver(this.scanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	}

	public void deregisterReceiver() {
		this.context.unregisterReceiver(this.scanReceiver);
	}
	
	private boolean setupWifi() {
		/* If wifi is enabled, then done with setup. Otherwise, enable it. */
		if (this.wifiManager.isWifiEnabled()) {
			return true;
		} else {
			Log.i("evie_debug", "" + R.string.wifi_enable_unsuccessful);
			this.wifiManager.setWifiEnabled(true);
		}
		
		/* If it's still not enabled, we have no power over what's wrong */
		if (!this.wifiManager.isWifiEnabled()) {
			Log.i("evie_debug", "" + R.string.wifi_enable_unsuccessful);
			return false;
		}

		/* Successfully enabled */
		Log.i("evie_debug", "" + R.string.wifi_enable_successful);
		return true;
	}

	public void startPeriodicScans() {
		Log.i("evie_debug", "starting wifi scan thread");
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		while (true) {
			/* If wifi wasn't enabled and setup wasn't successful, just ignore the click */
			if (!this.wifiManager.isWifiEnabled()) {
				if (!setupWifi()) {
					return;
				}
			}
	
			/* Wifi is setup correctly - check if scan is initiated */
			if (this.wifiManager.startScan()) {
				Log.i("evie_debug", "" + R.string.wifi_enable_successful);
			} else {
				Log.i("evie_debug", "" + R.string.wifi_enable_unsuccessful);
			}
			
			Log.i("evie_debug", "now sleeping ....");
			
			try {
				Thread.sleep(45000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
