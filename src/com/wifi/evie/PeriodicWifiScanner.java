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
	private final Toast toast;
	private final WifiManager wifiManager;
	private final WifiScanReceiver scanReceiver;

	public PeriodicWifiScanner (Context context) {
		this.context = context;
		this.wifiManager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
		this.scanReceiver = new WifiScanReceiver(this.wifiManager);

		this.toast = Toast.makeText(this.context, R.string.hello_world, Toast.LENGTH_LONG);
		this.toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
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
			this.toast.setText(R.string.wifi_enable_unsuccessful);
			this.toast.show();
			this.wifiManager.setWifiEnabled(true);
		}
		
		/* If it's still not enabled, we have no power over what's wrong */
		if (!this.wifiManager.isWifiEnabled()) {
			this.toast.setText(R.string.wifi_enable_unsuccessful);
			this.toast.show();
			return false;
		}

		/* Successfully enabled */
		this.toast.setText(this.context.getString(R.string.wifi_enable_successful));
		this.toast.show();
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
				this.toast.setText(R.string.wifi_scan_successful);
			} else {
				this.toast.setText(R.string.wifi_scan_unsuccessful);
			}
			this.toast.show();
			
			Log.i("evie_debug", "now sleeping ....");
			
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
