package org.dragons.scoutingapp.activities;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.dragons.scoutingapp.MatchFiles.Boolean;
import org.dragons.scoutingapp.MatchFiles.BooleanGroup;
import org.dragons.scoutingapp.MatchFiles.DataEntry;
import org.dragons.scoutingapp.MatchFiles.MatchData;
import org.dragons.scoutingapp.MatchFiles.Number;
import org.dragons.scoutingapp.MatchFiles.Text;
import org.dragons.scoutingapp.bluefiles.BlueThread;
import org.dragons.scoutingapp.R;
import org.dragons.scoutingapp.databinding.DataCollectionBinding;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Stephen Ogden on 5/27/17.
 * FRC 1595
 */
public class DataCollection extends AppCompatActivity {

	/**
	 * Since we cant store the individual widgets, just store their ids for future lookup.
	 *
	 * @deprecated Convert to Array
	 */
	@Deprecated
	private ArrayList<View> entries = new ArrayList();

	/**
	 * Documentation
	 */
	DataCollectionBinding binder;

	/**
	 * Changes the color of a provided number picker. See
	 * <a href='https://stackoverflow.com/questions/22962075/change-the-text-color-of-numberpicker'>this stackoverflow post for more info</a>.
	 *
	 * @param numberPicker The number picker object.
	 * @author Simon
	 */
	private static void setNumberPickerTextColor(@NonNull NumberPicker numberPicker) {

		/*
		try {
			Field selectorWheelPaintField = numberPicker.getClass().getDeclaredField("mSelectorWheelPaint");
			selectorWheelPaintField.setAccessible(true);
			((android.graphics.Paint) selectorWheelPaintField.get(numberPicker)).setColor(Color.WHITE);
		} catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException ignored) {
		}
		 */

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

		this.binder = DataBindingUtil.setContentView(this, R.layout.data_collection);

		int teamNumber = this.getIntent().getIntExtra("Team number", 0);

		// For a nice little accessibility feature we can set the top bar to display the team number that the user is scouting.
		// That way they don't forget, or scout the wrong team.
		this.setTitle(getResources().getString(R.string.teamToScout, teamNumber));

		// Comments
		this.binder.cancel.setOnClickListener(view -> this.finish());

		// Comments
		if (BlueThread.INSTANCE.getAutonomous() != null) {
			MatchData autonomousData = BlueThread.INSTANCE.getAutonomous();

			for (DataEntry entry : autonomousData.dataEntries) {
				this.parseData(entry);
			}
		}

		// Comments
		if (BlueThread.INSTANCE.getTeleOp() != null) {
			MatchData teleopData = BlueThread.INSTANCE.getTeleOp();

			for (DataEntry entry : teleopData.dataEntries) {
				this.parseData(entry);
			}
		}

		// Comments
		if (BlueThread.INSTANCE.getEndgame() != null) {
			MatchData endgameData = BlueThread.INSTANCE.getEndgame();

			for (DataEntry entry : endgameData.dataEntries) {
				this.parseData(entry);
			}
		}

		/*
		// First, add the autonomous section header
		this.contentView.addView(this.generateTextView("Autonomous:", 20,
				this.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT, 0,
						20, 0)));

		// Now add all the autonomous stuff
		try {
			for (DataEntry autonomousData : BlueThread.INSTANCE.getAutonomous().dataEntries) {
				this.parseData(autonomousData);
			}
		} catch (NullPointerException noConfig) {
			// TODO

		}

		// Add the teleop header
		this.contentView.addView(this.generateTextView("TeleOp:", 20,
				this.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT, 0,
						100, 0)));

		// Add the stuff for teleop
		try {
			for (DataEntry teleopData : BlueThread.INSTANCE.getTeleOp().dataEntries) {
				this.parseData(teleopData);
			}
		} catch (NullPointerException noConfig) {
			// TODO
		}

		// Add the end game header
		this.contentView.addView(this.generateTextView("End game:", 20,
				this.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT, 0,
						100, 0)));


		// Add end game stuff
		try {
			for (DataEntry endgameData : BlueThread.INSTANCE.getEndgame().dataEntries) {
				this.parseData(endgameData);
			}
		} catch (NullPointerException noConfig) {
			// TODO
		}

		// Comment section time
		this.contentView.addView(this.generateTextView("Additional feedback:", 20,
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

		// Setup the submit button listener
		findViewById(R.id.Submit).setOnClickListener(listener -> {
			// Gather all the data
			JSONObject data = new JSONObject();
			// Be sure to add the team number
			try {
				data.putOpt("Team number", teamNumber);
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
				BlueThread.INSTANCE.sendData(new BlueThreadRequest(BlueThreadRequest.Requests.DATA, data));
			} catch (NullPointerException noConnection) {
				// TODO
			}

			this.finish();
		});
		 */


	}

	/**
	 * Documentation
	 *
	 * @param text
	 * @param size
	 * @param width
	 * @param marginLeft
	 * @param marginTop
	 * @param marginRight
	 */
	private void generateTextView(String text, float size, int width, int marginLeft, int marginTop, int marginRight) {
		Log.d("generateTextView text", text);
		Log.d("generateTextView size", Float.toString(size));
		TextView textView = new TextView(this);
		textView.setText(text);
		textView.setTextSize(size);
		textView.setTextColor(Color.WHITE);
		textView.setGravity(Gravity.CENTER);
		textView.setLayoutParams(DataCollection.createLayoutParameters(width, marginLeft, marginTop, marginRight));
		textView.requestLayout();
		this.binder.content.addView(textView);
	}

	/**
	 * Generate a CheckBox widget, but due to centering issues, this needs to be placed in a LinearLayout.
	 *
	 * @param text             The text of the CheckBox.
	 * @param isChecked        Whether or not the CheckBox is checked by default.
	 * @param layoutParameters The LayoutParameters.
	 * @return The LinearLayout containing the generated CheckBox.
	 */
	@NonNull
	@Deprecated
	private LinearLayout generateCheckBox(String text, boolean isChecked, LinearLayout.LayoutParams layoutParameters) {
		Log.d("generateCheckBox text", text);
		Log.d("generateCheckbox", "CheckBox isChecked: " + isChecked);

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
		this.entries.add(checkBox);

		// Add the CheckBox to the LinearLayout, and return the LinearLayout
		linearLayout.addView(checkBox);
		return linearLayout;
	}

	/**
	 * Create and return a EditText widget.
	 *
	 * @param title            The title of the EditText (Not to be shown to the users. This is used for backend identification).
	 * @param defaultValue     The default text for the EditText.
	 * @param layoutParameters The LayoutParameters.
	 * @return The (custom) EditText widget.
	 */
	@Deprecated
	@NonNull
	private EditText generateTextField(String title, String defaultValue, LinearLayout.LayoutParams layoutParameters) {
		Log.d("generateTextField title", title);
		Log.d("default value", defaultValue);
		EditText text = new EditText(this);
		text.setText(defaultValue);
		text.setTag(title); // TODO Tag
		text.setTextSize(15);
		text.setGravity(Gravity.CENTER);
		text.setBackgroundColor(Color.DKGRAY);
		text.setTextColor(Color.WHITE);
		text.setLayoutParams(layoutParameters);
		text.requestLayout();
		this.entries.add(text);
		return text;
	}

	/**
	 * Create and return a NumberPicker widget that will be housed in a LinearLayout (for centering reasons).
	 *
	 * @param title            The title of the NumberPicker (not shown to the user. Used for backend identification).
	 * @param minValue         The minimum value for the NumberPicker.
	 * @param maxValue         The maximum value for the NumberPicker.
	 * @param step             How many units the NumberPicker increases/decreases by.
	 * @param defaultValue     The default value for the NumberPicker.
	 * @param layoutParameters The LayoutParameters.
	 * @return The (custom) NumberPicker, housed inside a LinearLayout.
	 */
	@Deprecated
	@NonNull
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
		NumberPicker spinner = new NumberPicker(this);
		spinner.setMinValue(minValue);
		spinner.setMaxValue(maxValue);
		spinner.setValue(defaultValue);
		spinner.setLayoutParams(layoutParameters);
		spinner.setBackgroundColor(Color.DKGRAY);
		spinner.setTag(title); // TODO Tag
		DataCollection.setNumberPickerTextColor(spinner);
		this.entries.add(spinner);

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
	@NonNull
	@Deprecated
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
		this.entries.add(button);
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
	@NonNull
	private static LinearLayout.LayoutParams createLayoutParameters(int width, int marginLeft, int marginTop, int marginRight) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(marginLeft, marginTop, marginRight, 0);
		return params;
	}

	/**
	 * Parse the match (config) data in order to dynamically generate the data_collection page.
	 *
	 * @param match The match data (from the config file).
	 */
	private void parseData(@NonNull DataEntry match) {

		if (match instanceof Text) {

			Text text = (Text) match;

			// Create the text input field (TextView and TextField).
			this.generateTextView(text.name, 17, LinearLayout.LayoutParams.MATCH_PARENT,
					0, 15, 0);
			this.binder.content.addView(this.generateTextField(text.name, text.value,
					DataCollection.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT,
							20, 5, 20)));


		} else if (match instanceof Number) {

			Number number = (Number) match;

			// Create the number input field (TextView and NumberPicker).
			this.generateTextView(number.name, 17, LinearLayout.LayoutParams.MATCH_PARENT,
					0, 15, 0);

			try {
				this.binder.content.addView(this.generateNumberPicker(number.name, number.minimumValue,
						number.maximumValue, number.stepValue, number.value, DataCollection.
								createLayoutParameters(LinearLayout.LayoutParams.WRAP_CONTENT,
								0, 5, 0)));
			} catch (NumberFormatException e) {
				Log.e("parseData", "Failed to add view to data collection", e);
			}
		} else if (match instanceof Boolean) {

			Boolean bool = (Boolean) match;

			// Create the boolean input field (CheckBox)
			this.binder.content.addView(this.generateCheckBox(bool.name, bool.value,
					DataCollection.createLayoutParameters(LinearLayout.LayoutParams.WRAP_CONTENT,
							0, 15, 0)));

		} else if (match instanceof BooleanGroup) {

			BooleanGroup booleanGroup = (BooleanGroup) match;

			// Create the boolean group input field (TextView and RadioGroup of RadioButtons)
			this.generateTextView(match.name, 17, LinearLayout.LayoutParams.MATCH_PARENT,
					0, 15, 0);

			// Get all the radio buttons in the value.
			/*
			RadioGroup group = new RadioGroup(this);
			try {
				JSONObject radioButtons = new JSONObject(booleanGroup.value[0].name);
				Log.d("Radio buttons", radioButtons.toString());
				Iterator<String> keys = radioButtons.keys();
				while (keys.hasNext()) {
					String key = keys.next();
					RadioButton button = this.generateRadioButton(key, DataCollection.
							createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT,
									0, 5, 0));
					group.addView(button);
					if (Boolean.parseBoolean(radioButtons.optString(key))) {
						group.check(button.getId());
					}
				}
			} catch (JSONException | NullPointerException e) {
				// TODO
				return;
			}
			this.binder.content.addView(group);
			 */
		}
	}
}
