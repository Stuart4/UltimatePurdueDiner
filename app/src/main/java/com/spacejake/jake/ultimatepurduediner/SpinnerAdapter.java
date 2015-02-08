
package com.spacejake.jake.ultimatepurduediner;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/*This file is part of UltimatePurdueDiner.

		UltimatePurdueDiner is free software: you can redistribute it and/or modify
		it under the terms of the GNU General Public License as published by
		the Free Software Foundation, either version 3 of the License, or
		(at your option) any later version.

		UltimatePurdueDiner is distributed in the hope that it will be useful,
		but WITHOUT ANY WARRANTY; without even the implied warranty of
		MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
		GNU General Public License for more details.

		You should have received a copy of the GNU General Public License
		along with UltimatePurdueDiner.  If not, see <http://www.gnu.org/licenses/>. */

public class SpinnerAdapter extends ArrayAdapter<Meal>{
	Context context;
	public SpinnerAdapter(Context context, Meal[] objects) {
		super(context, R.layout.spinner_layout, objects);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Meal meal = getItem(position);
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.spinner_layout, parent, false);

		String item = meal.toString();
		String hours = meal.getHours();
		TextView textView = (TextView) view.findViewById(R.id.spinnerMainTextView);
		TextView subTextView = (TextView) view.findViewById(R.id.spinnerSubTextView);
		textView.setText(item);
		if (meal.getIsServing()) {
			subTextView.setText(hours);
			textView.setTextColor(context.getResources().getColor(R.color.default_text_color_holo_light));
		} else {
			subTextView.setText("Not Serving");
		}


		return view;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		Meal meal = getItem(position);
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.spinner_layout, parent, false);
		}
		String item = meal.toString();
		String hours = meal.getHours();
		TextView textView = (TextView) convertView.findViewById(R.id.spinnerMainTextView);
		TextView subTextView = (TextView) convertView.findViewById(R.id.spinnerSubTextView);
		textView.setText(item);
		if (meal.getIsServing()) {
			subTextView.setText(hours);
			textView.setTextColor(context.getResources().getColor(R.color.default_text_color_holo_light));
		} else {
			subTextView.setText("Not Serving");
			textView.setTextColor(Color.parseColor("#ffff4444"));
		}
		return convertView;
	}
}
