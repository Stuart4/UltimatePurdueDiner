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
public class ListAdapter extends ArrayAdapter<MenuItem>{

	public ListAdapter(Context context, MenuItem[] items) {
		super(context, R.layout.menu_layout, items);
		if (getCount() == 0) {
			new AlertDialog.Builder(context).setTitle("Not Serving").setMessage("This meal is not being served.")
					.show();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.row_layout_list, parent, false);

		String item = getItem(position).toString();
		TextView textView = (TextView) view.findViewById(R.id.textView1);
		textView.setText(item);

		if (!getItem(position).getIsVegetarian()) {
			ImageView imageView = (ImageView) view.findViewById(R.id.leafImage);
			imageView.setVisibility(ImageView.INVISIBLE);
		}


		return view;
	}
}
