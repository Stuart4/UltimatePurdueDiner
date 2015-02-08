package com.spacejake.jake.ultimatepurduediner;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/*This file is part of PurdueFoodGrabber.

		PurdueFoodGrabber is free software: you can redistribute it and/or modify
		it under the terms of the GNU General Public License as published by
		the Free Software Foundation, either version 3 of the License, or
		(at your option) any later version.

		PurdueFoodGrabber is distributed in the hope that it will be useful,
		but WITHOUT ANY WARRANTY; without even the implied warranty of
		MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
		GNU General Public License for more details.

		You should have received a copy of the GNU General Public License
		along with PurdueFoodGrabber.  If not, see <http://www.gnu.org/licenses/>. */
public class BulkFoodGrabber {
	private final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
	private Calendar cal;
	private final String[] courts = new String[]{"ERHT", "FORD", "HILL", "WILY", "WIND"};

	public BulkFoodGrabber(Calendar cal) {
		this.cal = cal;
	}

	public BulkFoodGrabber() {
		cal = Calendar.getInstance();
	}

	public Menu[][] getMenus(int daysPast, int daysFuture) {
		Menu[][] menus = new Menu[daysFuture + daysPast + 1][5];
		cal.add(Calendar.DATE, -daysPast - 1);
		for (int i = 0; i < daysFuture + daysPast + 1; i++) {
			cal.add(Calendar.DATE, 1);
			String dateString = format.format(cal.getTime());
			for (int court = 0; court < courts.length; court++) {
				try {
					FoodGrabber grabber;
					if (cal != null) {
						grabber = new FoodGrabber(new URL(String.format("http://www.housing.purdue.edu/Menus/%s/%s",
								courts[court], dateString)), cal);
					} else {
						grabber = new FoodGrabber(new URL(String.format("http://www.housing.purdue.edu/Menus/%s/%s",
								courts[court], dateString)), Calendar.getInstance());
					}
					menus[i][court] = grabber.getFood();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return menus;
	}

	public static void main(String[] args) {
		new BulkFoodGrabber(new GregorianCalendar(2014, 13, 4)).getMenus(0, 0);
	}

}
