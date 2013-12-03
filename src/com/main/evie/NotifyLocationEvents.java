package com.main.evie;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.TextView;

import com.example.evie.R;

public class NotifyLocationEvents {
	Context mContext;
	int mNotificationID = 42;
	NotificationCompat.Builder mNotificationBuilder = null;
	NotificationManager mNotificationManager = null;
	public static final String CONTENT_TEXT = "You have events near your current location.";
	
	public NotifyLocationEvents(Context context) {
		mContext = context.getApplicationContext();
		mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationBuilder = new NotificationCompat.Builder(mContext)
				.setContentTitle("Events near your location")
				.setContentText(CONTENT_TEXT)
				.setSmallIcon(R.drawable.logo);
	}
	
	public void notifyEvents(int numEvents) {
		/*
		 * Usage:
		 * NotifyLocationEvents notification = new NotifyLocationEvents(getApplicationContext());
		 * notification.notifyEvents(42);
		 */
		mNotificationBuilder.setContentText("You have events near your current location.")
			.setNumber(numEvents);
		
		Intent eventIntent = new Intent(mContext, MainActivity.class);
		eventIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		PendingIntent eventPendingIntent =
		        PendingIntent.getActivity(mContext,
		        		0,
		        		eventIntent,
		        		PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mNotificationBuilder.setContentIntent(eventPendingIntent);
		mNotificationManager.notify(mNotificationID, mNotificationBuilder.build());
	}
}
