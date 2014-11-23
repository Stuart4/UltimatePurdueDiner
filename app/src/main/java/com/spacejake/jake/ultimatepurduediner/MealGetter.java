package com.spacejake.jake.ultimatepurduediner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import java.io.IOException;
import java.net.URL;

/**
 * Created by jake on 11/21/14.
 */
public class MealGetter extends AsyncTask<URL, Void, Menu> {

	private ProgressDialog dialog;
	private Context context;

	public MealGetter(Context context) {
		this.context = context;
		dialog = new ProgressDialog(context);
	}

	@Override
	protected void onPreExecute() {
		this.dialog = this.dialog.show(context, "Loading", "Grabbing menu...", true, false);
	}

	@Override
	protected void onPostExecute(Menu menu) {
		this.dialog.dismiss();
		((MainActivity) context).updateMenu(menu);
		super.onPostExecute(menu);
	}

	@Override
	protected Menu doInBackground(URL... urls) {
		try {
			FoodGrabber grabber = new FoodGrabber(urls[0]);
			return grabber.getFood();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
