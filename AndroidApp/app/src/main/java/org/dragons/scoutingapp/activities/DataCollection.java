package org.dragons.scoutingapp.activities;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import org.dragons.scoutingapp.bluefiles.BlueThreadRequest;
import org.dragons.scoutingapp.databinding.DataCollectionBinding;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Stephen Ogden on 5/27/17.
 * FRC 1595
 */
public class DataCollection extends AppCompatActivity {

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

		// Comments
		this.binder = DataBindingUtil.setContentView(this, R.layout.data_collection);

		// Comments
		int teamNumber = this.getIntent().getIntExtra("Team number", 0);

		// For a nice little accessibility feature we can set the top bar to display the team number that the user is scouting.
		// That way they don't forget, or scout the wrong team.
		this.setTitle(getResources().getString(R.string.teamToScout, teamNumber));

		// Comments
		this.binder.cancel.setOnClickListener(view -> this.finish());

		// Comments
		if (BlueThread.INSTANCE.getAutonomous() != null) {
			MatchData autonomousData = BlueThread.INSTANCE.getAutonomous();

			this.generateTextView("Autonomous", 20, ViewGroup.LayoutParams.MATCH_PARENT,
					0, 20, 0);

			for (int i = 0; i < autonomousData.dataEntries.length; i++) {
				autonomousData.dataEntries[i].view = this.parseData(autonomousData.dataEntries[i]);
			}
		}

		// Comments
		if (BlueThread.INSTANCE.getTeleOp() != null) {
			MatchData teleopData = BlueThread.INSTANCE.getTeleOp();

			this.generateTextView("TeleOp:", 20, LinearLayout.LayoutParams.MATCH_PARENT,
					0, 100, 0);
			for (int i = 0; i < teleopData.dataEntries.length; i++) {
				teleopData.dataEntries[i].view = this.parseData(teleopData.dataEntries[i]);
			}
		}

		// Comments
		if (BlueThread.INSTANCE.getEndgame() != null) {
			MatchData endgameData = BlueThread.INSTANCE.getEndgame();

			this.generateTextView("End game:", 20, LinearLayout.LayoutParams.MATCH_PARENT,
					0, 100, 0);
			for (int i = 0; i < endgameData.dataEntries.length; i++) {
				endgameData.dataEntries[i].view = this.parseData(endgameData.dataEntries[i]);
			}
		}

		// Comments
		this.binder.submit.setOnClickListener(view -> {

			// Gather all the data
			JSONObject data = new JSONObject();
			// Be sure to add the team number
			try {
				data.putOpt("Team number", teamNumber);
			} catch (JSONException jsonError) {
				// TODO
			}

			// TODO Pares the views and get the options!

			try {
				// Don't forget to add the comments!
				data.putOpt("Comments", String.format("%s", this.binder.additionalComments.
						getText().toString().replace(",", "，").
						replace(":", ";")));
			} catch (JSONException jsonError) {
				// TODO
			}

			Log.d("FullData", data.toString());
			BlueThread.INSTANCE.sendData(new BlueThreadRequest(BlueThreadRequest.Requests.DATA, data));
			this.finish();
		});
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
		button.setTextColor(Color.WHITE);
		button.setLayoutParams(layoutParameters);
		button.requestLayout();
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
	@Nullable
	private View parseData(@NonNull DataEntry match) {

		if (match instanceof Text) {

			Text text = (Text) match;

			// Create the text input field (TextView and TextField).
			this.generateTextView(text.name, 17, LinearLayout.LayoutParams.MATCH_PARENT,
					0, 15, 0);

			EditText textField = this.generateTextField(text.name, text.value,
					DataCollection.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT,
							20, 5, 20));
			this.binder.content.addView(textField);
			return textField;

		} else if (match instanceof Number) {

			Number number = (Number) match;

			// Create the number input field (TextView and NumberPicker).
			this.generateTextView(number.name, 17, LinearLayout.LayoutParams.MATCH_PARENT,
					0, 15, 0);

			LinearLayout numberPicker = this.generateNumberPicker(number.name, number.minimumValue,
					number.maximumValue, number.stepValue, number.value, DataCollection.
							createLayoutParameters(LinearLayout.LayoutParams.WRAP_CONTENT,
									0, 5, 0));

			this.binder.content.addView(numberPicker);
			return numberPicker;

		} else if (match instanceof Boolean) {

			Boolean bool = (Boolean) match;

			// Create the boolean input field (CheckBox)
			LinearLayout checkbox = this.generateCheckBox(bool.name, bool.value,
					DataCollection.createLayoutParameters(LinearLayout.LayoutParams.WRAP_CONTENT,
							0, 15, 0));

			this.binder.content.addView(checkbox);
			return checkbox;

		} else if (match instanceof BooleanGroup) {

			BooleanGroup booleanGroup = (BooleanGroup) match;

			// Create the boolean group input field (TextView and RadioGroup of RadioButtons)
			this.generateTextView(match.name, 17, LinearLayout.LayoutParams.MATCH_PARENT,
					0, 15, 0);

			// Get all the radio buttons in the value.
			RadioGroup group = new RadioGroup(this);

			for (Boolean booleanEntry : booleanGroup.value) {
				RadioButton button = this.generateRadioButton(booleanEntry.name, DataCollection.
						createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT,
								0, 5, 0));
				group.addView(button);
			}
			this.binder.content.addView(group);
			return group;
		}

		return null;
	}
}
