package com.spacejake.jake.ultimatepurduediner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import java.text.Format;
import java.text.SimpleDateFormat;
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

public class FloatingFood extends DialogFragment implements View.OnClickListener {
	private String foodName;
	private String meal;
	private String diningCourt;
	private String dateString;
	private short preference = 0;
	private PreferenceDataSource pds;

	static FloatingFood newInstance(String foodName, String diningCourt, String meal, Calendar cal) {
		FloatingFood ff = new FloatingFood();

		Bundle args = new Bundle();
		args.putString("name", foodName);
		args.putString("diningCourt", diningCourt);
		args.putString("meal", meal);
		Format dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		args.putString("dateString", dateFormat.format(cal.getTime()));
		ff.setArguments(args);

		return ff;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		foodName = getArguments().getString("name");
		meal = getArguments().getString("meal");
		diningCourt = getArguments().getString("diningCourt");
		dateString = getArguments().getString("dateString");
		final View floatingFood = getActivity().getLayoutInflater().inflate(R.layout.floating_food_layout, null);
		floatingFood.findViewById(R.id.sharingButton).setOnClickListener(this);
		final RadioButton toggleLike = (RadioButton) floatingFood.findViewById(R.id.radioLike);
		final RadioButton toggleDislike = (RadioButton) floatingFood.findViewById(R.id.radioDislike);
		final RadioButton toggleNoPref = (RadioButton) floatingFood.findViewById(R.id.radioNoPref);
		toggleDislike.setOnClickListener(this);
		toggleNoPref.setOnClickListener(this);
		toggleLike.setOnClickListener(this);
		ImageView imageLike = (ImageView) floatingFood.findViewById(R.id.imageLike);
		ImageView imageDislike = (ImageView) floatingFood.findViewById(R.id.imageDislike);
		imageDislike.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleDislike.callOnClick();
				toggleDislike.setChecked(true);
			}
		});
		imageLike.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				toggleLike.callOnClick();
				toggleLike.setChecked(true);
			}
		});
		new AsyncTask<Void, Void, Short>() {
			@Override
			protected Short doInBackground(Void... params) {
				try {
					pds = new PreferenceDataSource(getActivity());
					pds.open();
					return pds.getPref(foodName);
				} catch (Exception DropItLikeItsHot) {
				} finally {
					pds.close();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Short aShort) {
				preference = aShort;
				if (!toggleDislike.isChecked() && !toggleLike.isChecked() && !toggleNoPref.isChecked()) {
					if (aShort == 1) {
						toggleLike.setChecked(true);
					} else if (aShort == -1) {
						toggleDislike.setChecked(true);
					} else {
						toggleNoPref.setChecked(true);
					}
				}
			}
		}.execute();

		return new AlertDialog.Builder(getActivity())
				.setIcon(null)
				.setView(floatingFood)
				.setTitle(foodName)
				.setPositiveButton("save", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {

						new AsyncTask<Void, Void, Void>() {
							@Override
							protected Void doInBackground(Void... params) {
								try {
									pds = new PreferenceDataSource(getActivity());
									pds.open();
									if (preference == 1) {
										pds.like(foodName);
									} else if (preference == -1) {
										pds.dislike(foodName);
									} else {
										pds.noPref(foodName);
									}
									pds.close();
								} catch (Exception DropItLikeItsHot) {
								} finally {
									pds.close();
								}
								return null;
							}
						}.execute();
						((MainActivity) getActivity()).populateMenu();
					}
				})
				.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						getDialog().cancel();
					}
				})
				.create();
	}

	public void shareFood(View v) {
		Intent toShare = new Intent(Intent.ACTION_SEND);
		toShare.setType("text/plain");
		String message = String.format("%s has %s for %s on %s.", diningCourt, foodName, meal, dateString);
		toShare.putExtra(Intent.EXTRA_SUBJECT, String.format("%s at %s", foodName, diningCourt));
		toShare.putExtra(Intent.EXTRA_TEXT, message);
		if (toShare.resolveActivity(v.getContext().getPackageManager()) != null) {
			v.getContext().startActivity(toShare);
		} else {
			new AlertDialog.Builder(v.getContext()).setTitle("Could not share").setMessage("No suitable target found" +
					".").show();
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case (R.id.sharingButton):
				shareFood(view);
				break;
			case (R.id.radioLike):
				preference = 1;
				break;
			case (R.id.radioDislike):
				preference = -1;
				break;
			case (R.id.radioNoPref):
				preference = 0;
				break;
		}
	}
}
