package com.wifi.evie;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.evie.R;

public class WifiScanClickListener implements OnClickListener {

	private final Context context;
	private final Toast toast;
	private final WifiManager wifiManager;
	private final WifiScanReceiver scanReceiver;

	public WifiScanClickListener (Context context) {
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
			this.toast.setText(R.string.wifi_enable_successful);
			this.toast.show();
			return false;
		}

		/* Successfully enabled */
		this.toast.setText(this.context.getString(R.string.wifi_enable_successful));
		this.toast.show();
		return true;
	}
	
	@Override
	public void onClick(View view) {
		
		/* If wifi wasn't enabled and setup wasn't successful, just ignore the click */
		if (!this.wifiManager.isWifiEnabled()) {
			if (!setupWifi()) {
				return;
			}
		}

		/* Wifi is setup correctly */
		if (this.wifiManager.startScan()) {
			this.toast.setText(R.string.wifi_scan_successful);
		} else {
			this.toast.setText(R.string.wifi_scan_unsuccessful);
		}
		this.toast.show();
	}
}
