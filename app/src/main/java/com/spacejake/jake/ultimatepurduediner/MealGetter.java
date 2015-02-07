package com.spacejake.jake.ultimatepurduediner;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
            pds.like("Fish Square");
            pds.dislike("Manicotti");
            for (Meal meal : menu.getMeals()) {
                int likes = 0, dislikes = 0;
                MenuItem[] items = meal.getMenuItems();
                for (MenuItem item : meal.getMenuItems()) {
                    int pref = pds.getPref(item.toString());
                    if (pref == 1) {
                        likes++;
                        meal.likeItem(item);
                    } else if (pref == -1) {
                        dislikes++;
                        meal.dislikeItem(item);
                    }
                }
                meal.setNumLikes(likes);
                meal.setNumDislikes(dislikes);
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
