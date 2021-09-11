package org.dragons.scoutingapp.Activities;

import android.widget.TextView;
import org.dragons.scoutingapp.R;

import java.io.StringWriter;

/**
 * Created by Stephen Ogden on 6/24/18.
 * FRC 1595
 */
public class error_activity extends android.support.v7.app.AppCompatActivity {

	private static String errorMessage, stackTraceText;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.error_page);

		// Define the errorMessage
		((TextView) this.findViewById(R.id.errorType)).setText(String.format("Error message: %s", error_activity.errorMessage));

		// Define the stackTraceText
		((TextView) this.findViewById(R.id.stackText)).setText(error_activity.stackTraceText);

		// Add a listener to the return button
		this.findViewById(R.id.ReturnButton).setOnClickListener(v -> {
			data_collection.teamNumber = 0;
			// Reset the MAC address of the receiver.
			try {
				org.dragons.scoutingapp.BlueFiles.Bluetooth.close();
			} catch (Exception ignored) {

			}
			// Close the app
			System.exit(0);
		});
	}


	class CatchError {

		/**
		 * Catch any throwable objects, and launch the error activity page.
		 *
		 * @param context - The context of the activity
		 * @param e       The throwable (error)
		 */
		void Catch(android.content.Context context, Throwable e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new java.io.PrintWriter(sw));
			error_activity.errorMessage = e.toString();
			error_activity.stackTraceText = sw.toString();

			// Print the stack trace to logcat
			e.printStackTrace();

			// Start the error activity page
			context.startActivity(new android.content.Intent(context, error_activity.class));
		}
	}
}
