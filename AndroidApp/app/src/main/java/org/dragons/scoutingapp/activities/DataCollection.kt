package org.dragons.scoutingapp.activities

import android.annotation.SuppressLint
import android.graphics.Color
import org.dragons.scoutingapp.bluefiles.BlueThread
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import org.dragons.scoutingapp.R
import android.view.ViewGroup
import org.json.JSONObject
import org.dragons.scoutingapp.bluefiles.BlueThreadRequest
import android.view.Gravity
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.children
import com.shawnlin.numberpicker.NumberPicker
import org.dragons.scoutingapp.MatchFiles.Boolean
import org.dragons.scoutingapp.MatchFiles.DataEntry
import org.dragons.scoutingapp.MatchFiles.BooleanGroup
import org.dragons.scoutingapp.MatchFiles.Number
import org.dragons.scoutingapp.MatchFiles.Text
import org.dragons.scoutingapp.databinding.DataCollectionBinding

/**
 * Created by Stephen Ogden on 5/27/17.
 * FRC 1595
 */
class DataCollection : AppCompatActivity() {

	/**
	 * Documentation
	 */
	private lateinit var binder: DataCollectionBinding

	public override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		// Comments
		this.binder = DataBindingUtil.setContentView(this, R.layout.data_collection)

		// Comments
		val teamNumber = this.intent.getIntExtra("Team number", 0)

		// For a nice little accessibility feature we can set the top bar to display the team number that the user is scouting.
		// That way they don't forget, or scout the wrong team.
		this.title = resources.getString(R.string.teamToScout, teamNumber)

		// Comments
		this.binder.cancel.setOnClickListener { this.finish() }

		// Comments
		if (BlueThread.autonomous != null) {
			this.generateTextView("Autonomous", 20f, ViewGroup.LayoutParams.MATCH_PARENT,
				0, 20, 0)
			BlueThread.autonomous!!.dataEntries.forEach { it.view = this.setupView(it) }
		}

		// Comments
		if (BlueThread.teleOp != null) {
			this.generateTextView("TeleOp:", 20f, LinearLayout.LayoutParams.MATCH_PARENT,
				0, 100, 0)
			BlueThread.teleOp!!.dataEntries.forEach { it.view = this.setupView(it) }
		}

		// Comments
		if (BlueThread.endgame != null) {
			this.generateTextView("End game:", 20f, LinearLayout.LayoutParams.MATCH_PARENT,
				0, 100, 0)
			BlueThread.endgame!!.dataEntries.forEach { it.view = this.setupView(it) }
		}

		// Comments
		this.binder.submit.setOnClickListener {

			// Gather all the data
			var data = JSONObject()

			try {

				// Be sure to add the team number
				data.putOpt("Team number", teamNumber)

				// Comments
				if (BlueThread.autonomous != null) {
					data = getSendData(data, BlueThread.autonomous!!.dataEntries)
				}

				if (BlueThread.teleOp != null) {
					data = getSendData(data, BlueThread.teleOp!!.dataEntries)
				}

				if (BlueThread.endgame != null) {
					data = getSendData(data, BlueThread.endgame!!.dataEntries)
				}

				// Don't forget to add the comments!
				data.putOpt("Comments", this.binder.additionalComments.text.toString()
					.replace(",", "，").replace(":", ";"))
			} catch (NullPointerException : NullPointerException) {
				Log.e("onSubmit", "Unable to get data", NullPointerException)
			}

			Log.d("FullData", data.toString())
			BlueThread.sendData(BlueThreadRequest(BlueThreadRequest.Requests.DATA, data))
			this.finish()
		}
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
	private fun generateTextView(text: String, size: Float, width: Int, marginLeft: Int, marginTop: Int, marginRight: Int) {
		val textView = TextView(this)
		textView.text = text
		textView.textSize = size
		textView.setTextColor(Color.WHITE)
		textView.gravity = Gravity.CENTER
		textView.layoutParams = createLayoutParameters(width, marginLeft, marginTop, marginRight)
		textView.requestLayout()
		this.binder.content.addView(textView)
	}

	/**
	 * Parse the match (config) data in order to dynamically generate the data_collection page.
	 *
	 * @param match The match data (from the config file).
	 */
	private fun setupView(match: DataEntry<*, *>): View? {
		return when (match) {

			// Comments
			is Text -> {

				// Create the text input field (TextView and TextField).
				this.generateTextView(match.name, 17f, LinearLayout.LayoutParams.MATCH_PARENT,
					0, 15, 0)

				val layoutParameters = createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT,
					marginLeft = 20, marginTop = 5, marginRight = 20)

				val textField = EditText(this)
				textField.setText(match.value)
				textField.tag = match.name
				textField.textSize = TEXT_SIZE
				textField.gravity = Gravity.CENTER
				textField.setBackgroundColor(Color.DKGRAY)
				textField.setTextColor(Color.WHITE)
				textField.layoutParams = layoutParameters
				textField.requestLayout()

				this.binder.content.addView(textField)
				textField
			}

			// Comments
			is Number -> {

				// Create the number input field (TextView and NumberPicker).
				this.generateTextView(match.name, 17f, LinearLayout.LayoutParams.MATCH_PARENT,
					0, 15, 0)

				val layoutParameters = createLayoutParameters(LinearLayout.LayoutParams.WRAP_CONTENT,
					marginTop = 5)

				val numberPicker = NumberPicker(this)
				numberPicker.minValue = match.minimumValue
				numberPicker.maxValue = match.maximumValue
				numberPicker.value = match.value!!
				numberPicker.minimumWidth = LinearLayout.LayoutParams.MATCH_PARENT
				numberPicker.textSize = TEXT_SIZE
				numberPicker.selectedTextColor = Color.WHITE
				numberPicker.dividerColor = Color.DKGRAY
				numberPicker.textColor = Color.LTGRAY
				@SuppressLint("WrongConstant")
				numberPicker.orientation = NumberPicker.HORIZONTAL
				numberPicker.layoutParams = layoutParameters
				numberPicker.tag = match.name

				this.binder.content.addView(numberPicker)
				numberPicker
			}

			// Comments
			is Boolean -> {

				val layoutParameters = createLayoutParameters(LinearLayout.LayoutParams.WRAP_CONTENT,
					marginLeft = 15, marginTop = 15, gravity = Gravity.START)

				// Comments
				val checkBox = CheckBox(this)
				checkBox.text = match.name
				checkBox.textSize = TEXT_SIZE
				checkBox.isChecked = match.value!!
				checkBox.setTextColor(Color.WHITE)
				checkBox.layoutParams = layoutParameters
				checkBox.requestLayout()

				this.binder.content.addView(checkBox)
				checkBox
			}

			// Comments
			is BooleanGroup -> {

				// Create the boolean group input field (TextView and RadioGroup of RadioButtons)
				this.generateTextView(match.name, 17f, LinearLayout.LayoutParams.MATCH_PARENT,
					0, 15, 0)

				// Get all the radio buttons in the value.
				val group = RadioGroup(this)

				val layoutParameters = createLayoutParameters(LinearLayout.LayoutParams.MATCH_PARENT,
					marginLeft = 5, marginTop = 5)
				group.layoutParams = layoutParameters
				for (booleanEntry in match.value!!) {
					val button = RadioButton(this)
					button.text = booleanEntry.name
					button.textSize = TEXT_SIZE
					button.highlightColor = Color.LTGRAY
					button.setTextColor(Color.WHITE)
					button.layoutParams = layoutParameters
					button.requestLayout()
					group.addView(button)
				}
				this.binder.content.addView(group)
				group
			}

			// Comments
			else -> null
		}
	}

	companion object {

		/**
		 * Documentation
		 *
		 * @param jsonObject
		 * @param entries
		 * @return
		 */
		private fun getSendData(jsonObject: JSONObject, entries: Array<DataEntry<*, *>>): JSONObject {
			entries.forEach {
				when(it) {
					is Text -> jsonObject.putOpt(it.view!!.tag as String, it.view!!.text.toString())
					is Number -> jsonObject.putOpt(it.view!!.tag as String, it.view!!.value)
					is Boolean -> jsonObject.putOpt(it.view!!.text.toString(), it.view!!.isChecked)
					is BooleanGroup ->  {
						it.view!!.children.forEach { radioButtonView ->
							val radioButton = radioButtonView as RadioButton
							jsonObject.putOpt(radioButton.text.toString(), radioButton.isChecked)
						}
					}
				}
			}
			return jsonObject
		}

		/**
		 * Create LayoutParameters for margins and widget widths.
		 *
		 * @param width       The desired width of the widget.
		 * @param marginLeft  The desired left margin of the widget.
		 * @param marginTop   The desired top margin of the widget.
		 * @param marginRight The desired right margin of the widget.
		 * @param gravity Comments
		 * @return The generated LayoutParameters.
		 */
		private fun createLayoutParameters(width: Int, marginLeft: Int = 0, marginTop: Int = 0,
		                                   marginRight: Int = 0, gravity: Int = Gravity.CENTER):
				LinearLayout.LayoutParams {
			val params = LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT)
			params.setMargins(marginLeft, marginTop, marginRight, 0)
			params.gravity = gravity
			return params
		}

		/**
		 * Documentation
		 */
		const val TEXT_SIZE: Float = 15.0f
	}
}