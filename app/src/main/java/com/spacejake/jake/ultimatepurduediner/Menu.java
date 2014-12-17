package com.spacejake.jake.ultimatepurduediner;

import java.util.ArrayList;
import java.util.Calendar;

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

public class Menu {
	private ArrayList<Meal> meals = new ArrayList<Meal>();
	private String menuNote;
	private String name;
	private Calendar date;

	public Menu() {
		super();
	}

	public Menu (Calendar cal) {
		this.date = cal;
	}

	public void add(Meal meal) {
		meals.add(meal);
	}

	public Meal[] getMeals() {
		return meals.toArray(new Meal[meals.size()]);
	}

	public String getMenuNote() {
		return menuNote;
	}

	public void setMenuNote(String menuNote) {
		this.menuNote = menuNote;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

class Meal {
	private String name;
	private boolean isServing;
	private String hours;
	private ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();

	public void add(MenuItem menuItem) {
		menuItems.add(menuItem);
	}

	public void setServing(boolean serving) {
		isServing = serving;
		if (!serving) {
			hours = null;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getIsServing() {
		return isServing;
	}

	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
		isServing = true;
	}

	public MenuItem[] getMenuItems() {
		return menuItems.toArray(new MenuItem[menuItems.size()]);
	}

	public String toString() {
		return name;
	}
}

class MenuItem {
	private String name;
	private boolean isVegetarian;
	private int liked = 0;

	public void setVegetarian(boolean vegetarian) {
		isVegetarian = vegetarian;
	}

	public boolean getIsVegetarian() {
		return isVegetarian;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public int getLiked() {
		return liked;
	}
}
