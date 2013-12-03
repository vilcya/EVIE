package com.main.evie;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.evie.R;

public class EventListAdapter extends ArrayAdapter<String>{
	private Context context;
	private int resource;
	
	public EventListAdapter(Context context, int resource, String[] menu) {
		super(context, resource, menu);
		this.context = context;
		this.resource = resource;
	}

	@Override
	public View getView(int position, View row, ViewGroup parent) {

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) ((Activity) context).getLayoutInflater();
			row = inflater.inflate(this.resource, parent, false);
			
			TextView name = (TextView) row.findViewById(R.id.tv_menuname);
			String[] menu = ((MainActivity)this.context).getResources().getStringArray(R.array.drawermenu);
			name.setText(menu[position]);
		}

		return row;
	}
	
}
