package com.spacejake.jake.ultimatepurduediner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
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

public class FoodGrabber {
	private Document doc;
	private Calendar cal = Calendar.getInstance();

	public FoodGrabber(URL url) throws IOException {
		doc = Jsoup.connect(url.toString()).get();
	}

	public FoodGrabber(URL url, Calendar cal) throws IOException {
		this.cal = cal;
		doc = Jsoup.connect(url.toString()).get();
	}

	public Menu getFood() {
		Menu menu = null;
		if (cal == null) {
			menu = new Menu();
		} else {
			menu = new Menu(cal);
		}

		try {
			String menuName = doc.select("a.selected").first().text();
			menu.setName(menuName);
		} catch (NullPointerException ignore) {}

		try {
			String menuNotes = doc.select("div#menu-notes").first().text();
			menu.setMenuNote(menuNotes);
		} catch (NullPointerException ignore) {}

		menu.setDate(cal);

		Elements meals = doc.select("div.location-meal-container");
		for (Element mealElement : meals) {
			Meal meal = new Meal();
			meal.setName(mealElement.id());
			Element hours = mealElement.select("div.hours").first();
			if (hours.text().equals("Not Serving")) {
				meal.setServing(false);
			} else {
				meal.setServing(true);
				meal.setHours(hours.text());
			}
			Elements menuItems = mealElement.select("tr.menu-item");

			for (Element menuItemElement : menuItems) {
				MenuItem menuItem = new MenuItem();
				if (menuItemElement.select("td.veg").isEmpty()) {
					menuItem.setVegetarian(false);
				} else {
					menuItem.setVegetarian(true);
				}
				menuItem.setName(menuItemElement.text());
				meal.add(menuItem);
			}
			menu.add(meal);
		}
		return menu;
	}
}
