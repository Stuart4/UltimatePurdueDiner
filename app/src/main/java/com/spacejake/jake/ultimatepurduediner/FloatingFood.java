package com.spacejake.jake.ultimatepurduediner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.Inflater;

/**
 * Created by jake on 12/18/14.
 */
public class FloatingFood extends DialogFragment implements View.OnClickListener {
	private String foodName;
	private String meal;
	private String diningCourt;
	private String dateString;

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
//		((ImageView) floatingFood.findViewById(R.id.imageView)).setImageResource(R.drawable.earhart);
		((Button) floatingFood.findViewById(R.id.sharingButton)).setOnClickListener(this);
		final RadioButton toggleLike = (RadioButton) floatingFood.findViewById(R.id.radioLike);
		final RadioButton toggleDislike = (RadioButton) floatingFood.findViewById(R.id.radioDislike);
		RadioButton toggleNoPref = (RadioButton) floatingFood.findViewById(R.id.radioNoPref);
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
		return new AlertDialog.Builder(getActivity())
				.setIcon(null)
				.setView(floatingFood)
				.setTitle(foodName)
				.setNegativeButton("close", new DialogInterface.OnClickListener() {
					@Override
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
		switch(view.getId()) {
			case(R.id.sharingButton):
				shareFood(view);
				break;
			case(R.id.radioLike):
				break;
			case(R.id.radioDislike):
				break;
			case(R.id.radioNoPref):
				break;
		}
	}
}
