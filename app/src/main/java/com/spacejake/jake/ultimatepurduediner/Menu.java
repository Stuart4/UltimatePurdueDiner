package com.spacejake.jake.ultimatepurduediner;
import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Jacob Stuart on 11/22/14.
 */
public class Menu {
	private ArrayList<Meal> meals = new ArrayList<Meal>();

	public void add(Meal meal) {
		meals.add(meal);
	}

	public Meal[] getMeals() {
		return meals.toArray(new Meal[meals.size()]);
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

	public void setHours (String hours) {
		this.hours = hours;
		isServing = true;
	}

	public void setServing(boolean serving) {
		isServing = serving;
		if (!serving) {
			hours = null;
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}

	public boolean getIsServing() {
		return isServing;
	}

	public String getHours() {
		return hours;
	}

	public MenuItem[] getMenuItems() {
		return menuItems.toArray(new MenuItem[menuItems.size()]);
	}
}
class MenuItem {
	private String name;
	private boolean isVegetarian;

	public void setVegetarian(boolean vegetarian) {
		isVegetarian = vegetarian;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getIsVegetarian() {
		return isVegetarian;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name.replace("[A-Z][a-z]", "$1 $2");
	}
}
