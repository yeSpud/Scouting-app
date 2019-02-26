package org.frc1595Dragons.scoutingapp.Activities;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.frc1595Dragons.scoutingapp.BlueFiles.Bluetooth;
import org.frc1595Dragons.scoutingapp.BlueFiles.Request;
import org.frc1595Dragons.scoutingapp.MatchFiles.Match;
import org.frc1595Dragons.scoutingapp.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


/**
 * Created by Stephen Ogden on 5/27/17.
 * FRC 1595
 */
public class data_collection extends android.support.v7.app.AppCompatActivity {

	public static int teamNumber;

	// Since we cant store the individual widgets, just store their ids for future lookup
	private ArrayList<View> Nodes = new ArrayList<>();

	private LinearLayout contentView;

	// https://stackoverflow.com/questions/22962075/change-the-text-color-of-numberpicker
	public static void setNumberPickerTextColor(CustomNumberPicker numberPicker, int color) {

		try {
			java.lang.reflect.Field selectorWheelPaintField = numberPicker.getClass()
					.getDeclaredField("mSelectorWheelPaint");
			selectorWheelPaintField.setAccessible(true);
			((android.graphics.Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
		} catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
			// Do nothing :P
		}

		final int count = numberPicker.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = numberPicker.getChildAt(i);
			if (child instanceof EditText)
				((EditText) child).setTextColor(color);
		}
		numberPicker.invalidate();
	}

	@Override
	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.data_collection);

		// For a nice little accessibility feature, we can set the top bar to display the team teamNumber that the user is scouting
		// That way, they don't forget, or scout the wrong team :P
		this.setTitle(getResources().getString(R.string.teamToScout) + data_collection.teamNumber);


		// Get the scrollview section of the page to dynamically load the widgets for data collection
		contentView = findViewById(R.id.content);

		// First, add the autonomous section header
		contentView.addView(this.generateTextView("Autonomous:", 20,
				this.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT, 0,
						0, 0)));

		// Now add all the autonomous stuff
		try {
			for (Match.Autonomous autonomous : Bluetooth.matchData.autonomousData) {
				this.parseData(autonomous);
			}
		} catch (NullPointerException noConfig) {
			new error_activity().new CatchError().Catch(this, noConfig);
		}

		contentView.addView(this.generateTextView("TeleOp:", 20,
				this.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT, 0,
						15, 0)));

		// Add the stuff for teleop
		try {
			for (Match.TeleOp teleOp : Bluetooth.matchData.teleopData) {
				this.parseData(teleOp);
			}
		} catch (NullPointerException noConfig) {
			new error_activity().new CatchError().Catch(this, noConfig);
		}


		contentView.addView(this.generateTextView("End game:", 20,
				this.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT, 0,
						15, 0)));

		// Add end game stuff
		try {
			for (Match.Endgame endgame : Bluetooth.matchData.endgameData) {
				this.parseData(endgame);
			}
		} catch (NullPointerException noConfig) {
			new error_activity().new CatchError().Catch(this, noConfig);
		}


		contentView.addView(this.generateTextView("Additional feedback:", 20,
				this.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT, 0,
						15, 0)));

		final EditText comments = new EditText(this);
		comments.setBackgroundColor(Color.DKGRAY);
		comments.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_DONE);
		comments.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
		comments.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
		comments.setText("");
		comments.setTextColor(Color.WHITE);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 70);
		params.setMargins(0, 5, 0, 10);
		comments.setLayoutParams(params);
		contentView.addView(comments);


		findViewById(R.id.Cancel).setOnClickListener(listener -> this.finish());

		findViewById(R.id.Submit).setOnClickListener(listener -> {
			// Gather all the data
			JSONObject data = new JSONObject();
			// Be sure to add the team number
			try {
				data.putOpt("Team number", data_collection.teamNumber);
			} catch (JSONException jsonError) {
				new error_activity().new CatchError().Catch(this, jsonError);
			}

			// Add the data from the view
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
							str = Objects.requireNonNull(text.getText()).toString();
						} catch (NullPointerException NPE) {
							str  = "";
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
					new error_activity().new CatchError().Catch(this, jsonError);
				}
			}
			try {
				data.putOpt("Comments", String.format("%s", comments.getText().toString()));
			} catch (JSONException jsonError) {
				new error_activity().new CatchError().Catch(this, jsonError);
			}


			Log.d("FullData", data.toString());

			try {
				Bluetooth.bluetoothConnection.sendData(new Request(Request.Requests.DATA, data));
			} catch (NullPointerException noConnection) {
				new error_activity().new CatchError().Catch(this, noConnection);
			}

			this.finish();
		});


	}

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

	private LinearLayout generateCheckBox(String text, boolean isChecked, LinearLayout.LayoutParams layoutParameters) {
		Log.d("generateCheckBox text", text);
		Log.d("CheckBox isChecked", Boolean.toString(isChecked));
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		linearLayout.setGravity(Gravity.CENTER);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);

		CheckBox checkBox = new CheckBox(this);
		checkBox.setText(text);
		checkBox.setTextSize(15);
		checkBox.setChecked(isChecked);
		checkBox.setTextColor(Color.WHITE);
		checkBox.setLayoutParams(layoutParameters);
		checkBox.requestLayout();
		Nodes.add(checkBox);

		linearLayout.addView(checkBox);
		return linearLayout;
	}

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

	private LinearLayout generateNumberPicker(String title, int minValue, int maxValue, int step, int defaultValue, LinearLayout.LayoutParams layoutParameters) {
		Log.d("title", title);
		Log.d("minValue", Integer.toString(minValue));
		Log.d("maxValue", Integer.toString(maxValue));
		Log.d("step", Integer.toString(step));
		Log.d("defaultValue", Integer.toString(defaultValue));

		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		linearLayout.setGravity(Gravity.CENTER);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);

		CustomNumberPicker spinner = new CustomNumberPicker(this);
		spinner.setMinValue(minValue);
		spinner.setMaxValue(maxValue);
		spinner.setValue(defaultValue);
		spinner.setLayoutParams(layoutParameters);
		spinner.setBackgroundColor(Color.DKGRAY);
		spinner.setTitle(title);
		data_collection.setNumberPickerTextColor(spinner, Color.WHITE);
		Nodes.add(spinner);

		linearLayout.addView(spinner);

		return linearLayout;
	}

	private RadioButton generateRadioButton(String text, boolean isChecked, LinearLayout.LayoutParams layoutParameters) {
		Log.d("text", text);
		Log.d("isChecked", Boolean.toString(isChecked));
		RadioButton button = new RadioButton(this);
		button.setText(text);
		button.setTextSize(15);
		button.setChecked(isChecked);
		button.setHighlightColor(Color.LTGRAY);
		button.setBackgroundColor(Color.GRAY);
		button.setTextColor(Color.WHITE);
		button.setLayoutParams(layoutParameters);
		button.requestLayout();
		Nodes.add(button);
		return button;
	}

	private LinearLayout.LayoutParams createLayoutParameters(int width, int marginLeft, int marginTop, int marginRight) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(marginLeft, marginTop, marginRight, 0);
		return params;
	}

	private void parseData(org.frc1595Dragons.scoutingapp.MatchFiles.MatchBase match) {
		switch (match.datatype) {
			case Text:
				contentView.addView(this.generateTextView(match.name, 17, this.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT,
						0, 5, 0)));

				contentView.addView(this.generateTextField(match.name, match.value.get(0),
						this.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT,
								20, 0, 20)));
				break;
			case Number:
				contentView.addView(this.generateTextView(match.name, 17, this.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT,
						0, 5, 0)));

				contentView.addView(this.generateNumberPicker(match.name, Integer.parseInt(match.value.get(1)),
						Integer.parseInt(match.value.get(2)), Integer.parseInt(match.value.get(3)),
						Integer.parseInt(match.value.get(0)),
						this.createLayoutParameters(LinearLayout.LayoutParams.WRAP_CONTENT,
								0, 0, 0)));
				break;
			case Boolean:
				contentView.addView(this.generateCheckBox(match.name, Boolean.parseBoolean(match.value.get(0)),
						this.createLayoutParameters(LinearLayout.LayoutParams.WRAP_CONTENT,
								0, 5, 0)));
				break;
			case BooleanGroup:

				contentView.addView(this.generateTextView(match.name, 17,
						this.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT,
								0, 5, 0)));

				// Get all the radio buttons in the value
				RadioGroup group = new RadioGroup(this);
				try {
					JSONObject radioButtons = new JSONObject(match.value.get(0));
					while (radioButtons.keys().hasNext()) {
						String key = radioButtons.keys().next();
						group.addView(this.generateRadioButton(key, Boolean.parseBoolean(radioButtons.get(key).toString()),
								this.createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT,
										0, 0, 0)));
					}

				} catch (JSONException e) {
					new error_activity().new CatchError().Catch(this, e);
					return;
				}
				contentView.addView(group);
				break;
		}
	}

	private class CustomNumberPicker extends android.widget.NumberPicker {

		private String title;

		public CustomNumberPicker(Context context) {
			super(context);
		}

		public String getTitle() {
			return this.title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}

	private class CustomEditText extends android.support.v7.widget.AppCompatEditText {

		private String title;

		public CustomEditText(Context context) {
			super(context);
		}

		public String getTitle() {
			return this.title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}

}
