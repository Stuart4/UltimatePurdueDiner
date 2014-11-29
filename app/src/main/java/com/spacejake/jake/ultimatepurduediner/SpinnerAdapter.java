
package com.spacejake.jake.ultimatepurduediner;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jake on 11/25/14.
 */
public class SpinnerAdapter extends ArrayAdapter<Meal>{
	Context context;
	public SpinnerAdapter(Context context, Meal[] objects) {
		super(context, R.layout.spinner_layout, objects);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.spinner_layout, parent, false);

		String item = getItem(position).toString();
		String hours = getItem(position).getHours();
		TextView textView = (TextView) view.findViewById(R.id.spinnerMainTextView);
		TextView subTextView = (TextView) view.findViewById(R.id.spinnerSubTextView);
		textView.setText(item);
		if (hours != null) {
			subTextView.setText(hours);
		} else {
			subTextView.setText("Not Serving");
		}


		return view;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.spinner_layout, parent, false);
		}
		String item = getItem(position).toString();
		String hours = getItem(position).getHours();
		TextView textView = (TextView) convertView.findViewById(R.id.spinnerMainTextView);
		TextView subTextView = (TextView) convertView.findViewById(R.id.spinnerSubTextView);
		textView.setText(item);
		if (hours != null) {
			subTextView.setText(hours);
		} else {
			subTextView.setText("Not Serving");
		}
		return convertView;
	}
}
