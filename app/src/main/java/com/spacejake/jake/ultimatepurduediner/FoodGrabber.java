package com.spacejake.jake.ultimatepurduediner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;


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
public class FoodGrabber {
	Document doc;

	public FoodGrabber(URL url) throws IOException {
		doc = Jsoup.connect(url.toString()).get();
	}

	public Menu getFood() {
		Menu menu = new Menu();
		try {
			String menuNotes = doc.select("div#menu-notes").first().text();
			menu.setMenuNote(menuNotes);
		} catch (NullPointerException ignore) {
		}
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
