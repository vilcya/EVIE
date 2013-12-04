package com.main.evie;

import java.net.URL;

import android.graphics.BitmapFactory;

public class ImageDownloader implements Runnable {

	private final Event event;

	ImageDownloader(Event event) {
		this.event = event;
		new Thread(this).start();
	}

	@Override
	public void run() {
		URL imgUrl = this.event.getImgUrl();
		if (this.event.getImgUrl() == null) {
			this.event.setBitmap(null);
			return;
		}

		try {
			this.event.setBitmap(BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream()));
		} catch (Exception e) {
			e.printStackTrace();
			this.event.setBitmap(null);
		}
	}

}
