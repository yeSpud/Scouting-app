package org.dragons.scoutingapp.activities;

import android.widget.TextView;
import org.dragons.scoutingapp.R;
import org.dragons.scoutingapp.bluefiles.BlueThread;

import java.io.StringWriter;

/**
 * Created by Stephen Ogden on 6/24/18.
 * FRC 1595
 */
public class ErrorActivity extends androidx.appcompat.app.AppCompatActivity {

	private static String errorMessage, stackTraceText;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.error_page);

		// Define the errorMessage
		((TextView) this.findViewById(R.id.errorType)).setText(String.format("Error message: %s", ErrorActivity.errorMessage));

		// Define the stackTraceText
		((TextView) this.findViewById(R.id.stackText)).setText(ErrorActivity.stackTraceText);

		// Add a listener to the return button
		this.findViewById(R.id.ReturnButton).setOnClickListener(v -> {
			DataCollection.teamNumber = 0;
			// Reset the MAC address of the receiver.
			try {
				BlueThread.INSTANCE.close(true);
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
			ErrorActivity.errorMessage = e.toString();
			ErrorActivity.stackTraceText = sw.toString();

			// Print the stack trace to logcat
			e.printStackTrace();

			// Start the error activity page
			context.startActivity(new android.content.Intent(context, ErrorActivity.class));
		}
	}
}
