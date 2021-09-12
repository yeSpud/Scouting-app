package org.dragons.scoutingapp.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import org.dragons.scoutingapp.bluefiles.BlueThread;
import org.dragons.scoutingapp.bluefiles.Request;
import org.dragons.scoutingapp.MatchFiles.Match;
import org.dragons.scoutingapp.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by Stephen Ogden on 5/27/17.
 * FRC 1595
 */
public class DataCollection extends Activity {

	/**
	 * Variable for the team number.
	 */
	static int teamNumber;

	/**
	 * Since we cant store the individual widgets, just store their ids for future lookup.
	 */
	private ArrayList<View> Nodes = new ArrayList<>();

	private LinearLayout contentView;

	/**
	 * Changes the color of a provided number picker. See
	 * <a href='https://stackoverflow.com/questions/22962075/change-the-text-color-of-numberpicker'>this stackoverflow post for more info</a>.
	 *
	 * @param numberPicker The number picker object.
	 * @author Simon
	 */
	private static void setNumberPickerTextColor(CustomNumberPicker numberPicker) {

		try {
			java.lang.reflect.Field selectorWheelPaintField = numberPicker.getClass()
					.getDeclaredField("mSelectorWheelPaint");
			selectorWheelPaintField.setAccessible(true);
			((android.graphics.Paint) selectorWheelPaintField.get(numberPicker)).setColor(Color.WHITE);
		} catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException ignored) {
		}

		final int count = numberPicker.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = numberPicker.getChildAt(i);
			if (child instanceof EditText)
				((EditText) child).setTextColor(Color.WHITE);
		}
		numberPicker.invalidate();
	}

	@Override
	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.data_collection);

		// For a nice little accessibility feature, we can set the top bar to display the team teamNumber that the user is scouting
		// That way, they don't forget, or scout the wrong team :P
		this.setTitle(getResources().getString(R.string.teamToScout) + DataCollection.teamNumber);


		// Get the scrollview section of the page to dynamically load the widgets for data collection
		contentView = findViewById(R.id.content);

		// First, add the autonomous section header
		contentView.addView(this.generateTextView("Autonomous:", 20,
				this.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT, 0,
						20, 0)));

		// Now add all the autonomous stuff
		try {
			for (Match.Autonomous autonomous : BlueThread.INSTANCE.getMatchData().autonomousData) {
				this.parseData(autonomous);
			}
		} catch (NullPointerException noConfig) {
			// TODO

		}

		// Add the teleop header
		contentView.addView(this.generateTextView("TeleOp:", 20,
				this.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT, 0,
						100, 0)));

		// Add the stuff for teleop
		try {
			for (Match.TeleOp teleOp : BlueThread.INSTANCE.getMatchData().teleopData) {
				this.parseData(teleOp);
			}
		} catch (NullPointerException noConfig) {
			// TODO
		}

		// Add the end game header
		contentView.addView(this.generateTextView("End game:", 20,
				this.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT, 0,
						100, 0)));


		// Add end game stuff
		try {
			for (Match.Endgame endgame : BlueThread.INSTANCE.getMatchData().endgameData) {
				this.parseData(endgame);
			}
		} catch (NullPointerException noConfig) {
			// TODO
		}

		// Comment section time
		contentView.addView(this.generateTextView("Additional feedback:", 20,
				this.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT, 0,
						100, 0)));

		// Comments
		final EditText comments = new EditText(this);
		comments.setBackgroundColor(Color.DKGRAY);
		comments.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_DONE);
		comments.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
		comments.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
		comments.setText("");
		comments.setTextColor(Color.WHITE);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
		params.setMargins(0, 20, 0, 20);
		comments.setLayoutParams(params);
		contentView.addView(comments);


		// Setup the cancel button listener
		findViewById(R.id.Cancel).setOnClickListener(listener -> this.finish());

		// Setup the submit button listener
		findViewById(R.id.Submit).setOnClickListener(listener -> {
			// Gather all the data
			JSONObject data = new JSONObject();
			// Be sure to add the team number
			try {
				data.putOpt("Team number", DataCollection.teamNumber);
			} catch (JSONException jsonError) {
				// TODO
			}

			// Get the data from the view
			for (View view : Nodes) {
				try {
					if (view instanceof CheckBox) {
						CheckBox box = (CheckBox) view;
						Log.d("Adding checkbox", box.getText().toString());
						data.putOpt(box.getText().toString(), box.isChecked() ? 1 : 0);
					} else if (view instanceof CustomNumberPicker) {
						CustomNumberPicker picker = (CustomNumberPicker) view;
						Log.d("Adding number", picker.getTitle());
						data.putOpt(picker.getTitle(), picker.getValue());
					} else if (view instanceof CustomEditText) {
						CustomEditText text = (CustomEditText) view;
						Log.d("Adding text", text.getTitle());
						String str;
						try {
							str = java.util.Objects.requireNonNull(text.getText()).toString().replace(",", "，");
						} catch (NullPointerException NPE) {
							str = "";
						}
						data.putOpt(text.getTitle(), String.format("%s", str));
					} else if (view instanceof RadioButton) {
						RadioButton button = (RadioButton) view;
						Log.d("Adding radio button", button.getText().toString());
						data.putOpt(button.getText().toString(), button.isChecked() ? 1 : 0);
					} else {
						Log.w("Unrecognized class", view.getClass().getName());
					}
				} catch (JSONException jsonError) {
					// TODO
				}
			}
			try {
				// Don't forget to add the comments!
				data.putOpt("Comments", String.format("%s", comments.getText().toString().replace(",", "，").replace(":", ";")));
			} catch (JSONException jsonError) {
				// TODO
			}


			Log.d("FullData", data.toString());

			try {
				BlueThread.INSTANCE.sendData(new Request(Request.Requests.DATA, data));
			} catch (NullPointerException noConnection) {
				// TODO
			}

			this.finish();
		});


	}

	/**
	 * Generate and return a TextView widget.
	 *
	 * @param text             The default text.
	 * @param size             The size of the font.
	 * @param layoutParameters The layout parameters.
	 * @return The resulting TextView widget.
	 */
	private TextView generateTextView(String text, float size, LinearLayout.LayoutParams layoutParameters) {
		Log.d("generateTextView text", text);
		Log.d("generateTextView size", Float.toString(size));
		TextView textView = new TextView(this);
		textView.setText(text);
		textView.setTextSize(size);
		textView.setTextColor(Color.WHITE);
		textView.setGravity(Gravity.CENTER);
		textView.setLayoutParams(layoutParameters);
		textView.requestLayout();
		return textView;
	}

	/**
	 * Generate a CheckBox widget, but due to centering issues, this needs to be placed in a LinearLayout.
	 *
	 * @param text             The text of the CheckBox.
	 * @param isChecked        Whether or not the CheckBox is checked by default.
	 * @param layoutParameters The LayoutParameters.
	 * @return The LinearLayout containing the generated CheckBox.
	 */
	private LinearLayout generateCheckBox(String text, boolean isChecked, LinearLayout.LayoutParams layoutParameters) {
		Log.d("generateCheckBox text", text);
		Log.d("CheckBox isChecked", Boolean.toString(isChecked));

		// First create the LinearLayout that will house the CheckBox
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		linearLayout.setGravity(Gravity.CENTER);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);

		// Now create the Checkbox
		CheckBox checkBox = new CheckBox(this);
		checkBox.setText(text);
		checkBox.setTextSize(15);
		checkBox.setChecked(isChecked);
		checkBox.setTextColor(Color.WHITE);
		checkBox.setLayoutParams(layoutParameters);
		checkBox.requestLayout();
		Nodes.add(checkBox);

		// Add the CheckBox to the LinearLayout, and return the LinearLayout
		linearLayout.addView(checkBox);
		return linearLayout;
	}

	/**
	 * Create and return a (custom) EditText widget.
	 *
	 * @param title            The title of the EditText (Not to be shown to the users. This is used for backend identification).
	 * @param defaultValue     The default text for the EditText.
	 * @param layoutParameters The LayoutParameters.
	 * @return The (custom) EditText widget.
	 */
	private CustomEditText generateTextField(String title, String defaultValue, LinearLayout.LayoutParams layoutParameters) {
		Log.d("generateTextField title", title);
		Log.d("default value", defaultValue);
		CustomEditText text = new CustomEditText(this);
		text.setText(defaultValue);
		text.setTitle(title);
		text.setTextSize(15);
		text.setGravity(Gravity.CENTER);
		text.setBackgroundColor(Color.DKGRAY);
		text.setTextColor(Color.WHITE);
		text.setLayoutParams(layoutParameters);
		text.requestLayout();
		Nodes.add(text);
		return text;
	}

	/**
	 * Create and return a (custom) NumberPicker widget that will be housed in a LinearLayout (for centering reasons).
	 *
	 * @param title            The title of the NumberPicker (not shown to the user. Used for backend identification).
	 * @param minValue         The minimum value for the NumberPicker.
	 * @param maxValue         The maximum value for the NumberPicker.
	 * @param step             How many units the NumberPicker increases/decreases by.
	 * @param defaultValue     The default value for the NumberPicker.
	 * @param layoutParameters The LayoutParameters.
	 * @return The (custom) NumberPicker, housed inside a LinearLayout.
	 */
	private LinearLayout generateNumberPicker(String title, int minValue, int maxValue, int step, int defaultValue, LinearLayout.LayoutParams layoutParameters) {
		Log.d("title", title);
		Log.d("minValue", Integer.toString(minValue));
		Log.d("maxValue", Integer.toString(maxValue));
		Log.d("step", Integer.toString(step));
		Log.d("defaultValue", Integer.toString(defaultValue));

		// First create the LinearLayout
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		linearLayout.setGravity(Gravity.CENTER);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);

		// Now create the custom NumberPicker
		CustomNumberPicker spinner = new CustomNumberPicker(this);
		spinner.setMinValue(minValue);
		spinner.setMaxValue(maxValue);
		spinner.setValue(defaultValue);
		spinner.setLayoutParams(layoutParameters);
		spinner.setBackgroundColor(Color.DKGRAY);
		spinner.setTitle(title);
		DataCollection.setNumberPickerTextColor(spinner);
		Nodes.add(spinner);

		// Add the custom NumberPicker to the LinearLayout, and return the LinearLayout.
		linearLayout.addView(spinner);
		return linearLayout;
	}

	/**
	 * Create and return a RadioButton.
	 *
	 * @param text             The text of the RadioButton.
	 * @param layoutParameters The LayoutParameters.
	 * @return The RadioButton.
	 */
	private RadioButton generateRadioButton(String text, LinearLayout.LayoutParams layoutParameters) {
		Log.d("text", text);
		RadioButton button = new RadioButton(this);
		button.setText(text);
		button.setTextSize(15);
		button.setHighlightColor(Color.LTGRAY);
		button.setBackgroundColor(Color.GRAY);
		button.setTextColor(Color.WHITE);
		button.setLayoutParams(layoutParameters);
		button.requestLayout();
		Nodes.add(button);
		return button;
	}

	/**
	 * Create LayoutParameters for margins and widget widths.
	 *
	 * @param width       The desired width of the widget.
	 * @param marginLeft  The desired left margin of the widget.
	 * @param marginTop   The desired top margin of the widget.
	 * @param marginRight The desired right margin of the widget.
	 * @return The generated LayoutParameters.
	 */
	private LinearLayout.LayoutParams createLayoutParameters(int width, int marginLeft, int marginTop, int marginRight) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(marginLeft, marginTop, marginRight, 0);
		return params;
	}

	/**
	 * Parse the match (config) data in order to dynamically generate the data_collection page.
	 *
	 * @param match The match data (from the config file).
	 */
	private void parseData(org.dragons.scoutingapp.MatchFiles.MatchBase match) {
		switch (match.datatype) {
			case Text:
				// Create the text input field (TextView and TextField).
				contentView.addView(this.generateTextView(match.name, 17, this.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT,
						0, 15, 0)));
				contentView.addView(this.generateTextField(match.name, match.value.get(0),
						this.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT,
								20, 5, 20)));
				break;
			case Number:
				// Create the number input field (TextView and NumberPicker).
				contentView.addView(this.generateTextView(match.name, 17, this.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT,
						0, 15, 0)));
				contentView.addView(this.generateNumberPicker(match.name, Integer.parseInt(match.value.get(1)),
						Integer.parseInt(match.value.get(2)), Integer.parseInt(match.value.get(3)),
						Integer.parseInt(match.value.get(0)),
						this.createLayoutParameters(LinearLayout.LayoutParams.WRAP_CONTENT,
								0, 5, 0)));
				break;
			case Boolean:
				// Create the boolean input field (CheckBox)/
				contentView.addView(this.generateCheckBox(match.name, Boolean.parseBoolean(match.value.get(0)),
						this.createLayoutParameters(LinearLayout.LayoutParams.WRAP_CONTENT,
								0, 15, 0)));
				break;
			case BooleanGroup:
				// Create the boolean group input field (TextView and RadioGroup of RadioButtons)
				contentView.addView(this.generateTextView(match.name, 17,
						this.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT,
								0, 15, 0)));

				// Get all the radio buttons in the value.
				RadioGroup group = new RadioGroup(this);
				try {
					JSONObject radioButtons = new JSONObject(match.value.get(0));
					Log.d("Radio buttons", radioButtons.toString());
					Iterator<String> keys = radioButtons.keys();
					while (keys.hasNext()) {
						String key = keys.next();
						RadioButton button = this.generateRadioButton(key, this.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT,
								0, 5, 0));
						group.addView(button);
						if (Boolean.parseBoolean(radioButtons.optString(key))) {
							group.check(button.getId());
						}
					}
				} catch (JSONException e) {
					// TODO
					return;
				}

				contentView.addView(group);
				break;
		}
	}

	/**
	 * A custom NumberPicker class that takes the standard NumberPicker and adds a title object to it for backend identification.
	 */
	private class CustomNumberPicker extends NumberPicker {

		private String title;

		/**
		 * Constructor for the custom NumberPicker.
		 *
		 * @param context The application context (usually <code>this</code>).
		 */
		public CustomNumberPicker(Context context) {
			super(context);
		}

		/**
		 * Returns the title of the NumberPicker.
		 *
		 * @return The title.
		 */
		public String getTitle() {
			return this.title;
		}

		/**
		 * Set the title of the NumberPicker.
		 *
		 * @param title The title.
		 */
		public void setTitle(String title) {
			this.title = title;
		}
	}

	/**
	 * A custom EditText class that takes the standard EditText and adds a title object to it for backend identification.
	 */
	private class CustomEditText extends androidx.appcompat.widget.AppCompatEditText {

		private String title;

		/**
		 * Constructor for the custom EditText.
		 *
		 * @param context The application context (usually <code>this</code>).
		 */
		public CustomEditText(Context context) {
			super(context);
		}

		/**
		 * Returns the title of the EditText.
		 *
		 * @return The title.
		 */
		public String getTitle() {
			return this.title;
		}

		/**
		 * Set the title of the EditText.
		 *
		 * @param title The title.
		 */
		public void setTitle(String title) {
			this.title = title;
		}
	}

}
