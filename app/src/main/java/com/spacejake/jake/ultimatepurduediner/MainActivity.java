package com.spacejake.jake.ultimatepurduediner;

import android.app.FragmentManager;
import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;


public class MainActivity extends Activity
		implements NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private String courtShort;

	private String dateString;

	private com.spacejake.jake.ultimatepurduediner.Menu foodMenu = new com.spacejake.jake.ultimatepurduediner.Menu();

	private Spinner spinner;
	private ListView listView;

	private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
	private Calendar cal;






	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = getTitle();
		mNavigationDrawerFragment = (NavigationDrawerFragment)
				getFragmentManager().findFragmentById(R.id.navigation_drawer);

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(
				R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		cal = Calendar.getInstance();
		dateString = format.format(cal.getTime());

		spinner = (Spinner) findViewById(R.id.spinner);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				updateMeal();
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		listView = (ListView) findViewById(R.id.listview);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				String foodName = adapterView.getItemAtPosition(i).toString();
				String meal = spinner.getSelectedItem().toString();

				FragmentTransaction ft = getFragmentManager().beginTransaction();
				Fragment previous = getFragmentManager().findFragmentByTag("floatingFood");

				if (previous != null) {
					ft.remove(previous);
				}

				ft.addToBackStack(null);

				DialogFragment frag = FloatingFood.newInstance(foodName, mTitle.toString(), meal, cal);
				frag.show(ft, "floatingFood");
			}
		});


	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
				.commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
			case 1:
				mTitle = getString(R.string.Earhart);
				courtShort = "ERHT";
				break;
			case 2:
				mTitle = getString(R.string.Ford);
				courtShort = "FORD";
				break;
			case 3:
				mTitle = getString(R.string.Hillenbrand);
				courtShort = "HILL";
				break;
			case 4:
				mTitle = getString(R.string.Wiley);
				courtShort = "WILY";
				break;
			case 5:
				mTitle = getString(R.string.Windsor);
				courtShort = "WIND";
				break;
		}
		populateMenu();
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section
		 * number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(
					getArguments().getInt(ARG_SECTION_NUMBER));
		}
	}

	public void dateChange(int year, int month, int day) {
		cal.set(year, month, day);
		dateString = format.format(cal.getTime());
		populateMenu();
	}

	public void populateMenu() {
		try {
			URL hfs = null;
			if (courtShort != null) {
				hfs = new URL(String.format("http://www.housing.purdue.edu/Menus/%s/%s", courtShort, dateString));
			} else {
				hfs = new URL("http://www.housing.purdue.edu/Menus/ERHT");
			}
			MealGetter t = new MealGetter(this);
			t.execute(hfs);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateMenu(com.spacejake.jake.ultimatepurduediner.Menu foodMenu, int selection) {
		this.foodMenu = foodMenu;
		SpinnerAdapter spinnerAdapter = new SpinnerAdapter(MainActivity.this, foodMenu.getMeals());
		spinner.setAdapter(spinnerAdapter);
		spinnerAdapter.notifyDataSetChanged();
        spinner.setSelection(selection);
		if (foodMenu.getMenuNote() != null) {
			new AlertDialog.Builder(this).setTitle("A Special Note Was Found!").setMessage(foodMenu.getMenuNote())
					.show();
		}
	}

	public void updateMeal() {
		ListAdapter listAdapter = new ListAdapter(MainActivity.this,foodMenu.getMeals()[spinner
				.getSelectedItemPosition()].getMenuItems());
		listView.setAdapter(listAdapter);
		listAdapter.notifyDataSetChanged();
	}

}
