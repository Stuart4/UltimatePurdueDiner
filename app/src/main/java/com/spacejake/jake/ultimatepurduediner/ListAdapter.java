package com.spacejake.jake.ultimatepurduediner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

public class ListAdapter extends ArrayAdapter<MenuItem>{

	public ListAdapter(Context context, MenuItem[] items) {
		super(context, R.layout.row_layout_list, items);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.row_layout_list, parent, false);

		String item = getItem(position).toString();
		TextView textView = (TextView) view.findViewById(R.id.listTextView);
		textView.setText(item);

		if (getItem(position).getIsVegetarian()) {
			ImageView imageView = (ImageView) view.findViewById(R.id.leafImage);
			imageView.setVisibility(ImageView.VISIBLE);
		}

		if (getItem(position).getLiked() == 1) {
			ImageView imageView = (ImageView) view.findViewById(R.id.like);
			imageView.setVisibility(ImageView.VISIBLE);
		}

		if (getItem(position).getLiked() == -1) {
			ImageView imageView = (ImageView) view.findViewById(R.id.like);
			imageView.setImageResource(R.drawable.dislike);
			imageView.setVisibility(ImageView.VISIBLE);
		}

		return view;
	}
}
