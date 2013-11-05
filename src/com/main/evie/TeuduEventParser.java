package com.main.evie;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class TeuduEventParser {
	private static final String ns = null;
	private DynamicEventList events = null;
	
	public TeuduEventParser() {
		events = new DynamicEventList();
	}
	
	public DynamicEventList parse(InputStream in) throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readEvents(parser);
		} catch(XmlPullParserException e) {
			System.out.println(e.getMessage());
			return null;
		} finally {
			in.close();
		}
	}
	
	private DynamicEventList readEvents(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "events");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("event")) {
				readEvent(parser);
			} else {
				skip(parser);
			}
		}
		return events;
	}
	
	private void readEvent(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "event");
		int id = -1;
		String name = null;
		String description = null;
		Date startTime = null;
		Date endTime = null;
		String location = null;
		String imgUrl = null;
		String categories = null;
		boolean cancelled = true;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
		
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String tag = parser.getName();
			if (tag.equals("id")) {
				id = readID(parser);
			} else if (tag.equals("name")) {
				name = readName(parser);
			} else if (tag.equals("description")) {
				description = readDescription(parser);
			} else if (tag.equals("summary")) {
				/* Ignore the summary */
				skip(parser);
			} else if (tag.equals("starttime")) {
				try {
					startTime = dateFormat.parse(readStarttime(parser));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else if (tag.equals("endtime")) {
				try {
					endTime = dateFormat.parse(readEndtime(parser));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else if (tag.equals("location")) {
				location = readLocation(parser);
			} else if (tag.equals("image")) {
				imgUrl = readImage(parser);
			} else if (tag.equals("categories")) {
				categories = readCategories(parser);
			} else if (tag.equals("cancelled")) {
				cancelled = readCancelled(parser);
			} else {
				skip(parser);
			}
		}

		events.createEvent(id, name, description, null, null, 
				location, imgUrl, categories, cancelled);
	}
	
	private int readID(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "id");
		int id = readInt(parser);
		parser.require(XmlPullParser.END_TAG, ns, "id");
		return id;
	}
	
	private String readName(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "name");
		String name = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "name");
		return name;
	}
	
	private String readDescription(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "description");
		String description = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "description");
		return description;
	}
	
	private String readSummary(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "summary");
		String summary = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "summary");
		return summary;
	}
	
	private String readStarttime(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "starttime");
		String starttime = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "starttime");
		return starttime;
	}
	
	private String readEndtime(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "endtime");
		String endtime = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "endtime");
		return endtime;
	}
	
	private String readLocation(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "location");
		String location = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "location");
		return location;
	}
	
	private String readImage(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "image");
		String image = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "image");
		return image;
	}
	
	private String readCategories(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "categories");
		String categories = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "categories");
		return categories;
	}
	
	private boolean readCancelled(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "cancelled");
		boolean cancelled = readBoolean(parser);
		parser.require(XmlPullParser.END_TAG, ns, "cancelled");
		return cancelled;
	}
	
	private int readInt(XmlPullParser parser) throws XmlPullParserException, IOException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		
		return Integer.parseInt(result);
	}
	
	private boolean readBoolean(XmlPullParser parser) throws XmlPullParserException, IOException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}

		if (result.equals("true")) {
			return true;
		}
		
		return false;
	}
	
	private String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}
	
	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}
	
}
