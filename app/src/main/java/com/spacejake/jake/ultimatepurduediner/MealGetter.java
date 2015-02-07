package com.spacejake.jake.ultimatepurduediner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;

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
		try {
            int selection = 0;
            Calendar cal = Calendar.getInstance();
            int curr = cal.get(Calendar.HOUR_OF_DAY) * 12 + cal.get(Calendar.MINUTE);
            for (int i = menu.getMeals().length - 1; i > -1; i++) {
                int[] timeRange = strToRange(menu.getMeals()[i].getHours());
                if (curr >= timeRange[0]) {
                    selection = i;
                    break;
                }
            }
            ((MainActivity) context).updateMenu(menu, selection);
        } catch (NumberFormatException|ArrayIndexOutOfBoundsException e) {
            ((MainActivity) context).updateMenu(menu, 2);
		} catch (Exception e) {
			new AlertDialog.Builder(context).setTitle("Error")
					.setMessage("Purdue's housing and food service cannot be contacted.")
					.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							((MainActivity) context).populateMenu();
						}
					}).show();
			super.onPostExecute(menu);
		}
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

    //example input: 11:00 a.m. - 2:00 p.m.
    //example output: {660, 840}
    public static int[] strToRange (String input) {
        int[] output = new int[2];
        input = input.replace(" ", "");
        input = input.replace(":", "");
        String[] parts = input.split("-");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].length() == 9) {
                output[i] = Integer.parseInt(parts[i].substring(0, 2)) * 12 +
                        Integer.parseInt(parts[i].substring(3, 5)) +
                        parts[i].charAt(5) == 'p'? 720 : 0;
            } else {
                output[i] = Integer.parseInt(parts[i].substring(0, 1)) * 12 +
                        Integer.parseInt(parts[i].substring(2, 4)) +
                        parts[i].charAt(4) == 'p'? 720 : 0;
            }
        }
        return output;
    }
}
