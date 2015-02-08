package com.spacejake.jake.ultimatepurduediner;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import java.net.URL;
import java.util.Calendar;

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
            int curr = cal.get(Calendar.HOUR) * 12 + cal.get(Calendar.MINUTE) + cal.get(Calendar.AM_PM) * 720;
            int lastFoundSelection = 0;
            for (int i = 0; i < menu.getMeals().length; i++) {
                if (!menu.getMeals()[i].getIsServing()) {
                    continue;
                }
                lastFoundSelection = i;
                int[] timeRange = strToRange(menu.getMeals()[i].getHours());
                if (curr < timeRange[1]) {
                    selection = i;
                    break;
                }
            }
            if (menu.getMeals()[selection].getIsServing())
                ((MainActivity) context).updateMenu(menu, selection);
            else
                ((MainActivity) context).updateMenu(menu, lastFoundSelection);
        } catch (NumberFormatException|ArrayIndexOutOfBoundsException e) {
            ((MainActivity) context).updateMenu(menu, 0);
            e.printStackTrace();
		} catch (Exception e) {
            e.printStackTrace();
			new AlertDialog.Builder(context).setTitle("Error")
					.setMessage("Purdue's housing and food services cannot be contacted.")
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
            Menu menu = grabber.getFood();
            PreferenceDataSource pds = new PreferenceDataSource(context);
            pds.open();
            for (Meal meal : menu.getMeals()) {
                MenuItem[] items = meal.getMenuItems();
                for (MenuItem item : meal.getMenuItems()) {
                    int pref = pds.getPref(item.toString());
                    if (pref == 1) {
                        meal.likeItem(item);
                    } else if (pref == -1) {
                        meal.dislikeItem(item);
                    }
                }
            }
            pds.close();
            return menu;
		} catch (Exception e) {
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
            if (parts[i].length() == 8) {
                output[i] += Integer.parseInt(parts[i].substring(0, 2)) * 12;
                output[i] += Integer.parseInt(parts[i].substring(2, 4));
                output[i] += parts[i].charAt(4) == 'p'? 720 : 0;
            } else {
                output[i] += Integer.parseInt(parts[i].substring(0, 1)) * 12;
                output[i] += Integer.parseInt(parts[i].substring(1, 3));
                output[i] += parts[i].charAt(3) == 'p'? 720 : 0;
            }
        }
        return output;
    }
}
