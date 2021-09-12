package org.dragons.scoutingapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.CaptivePortal;
import android.os.Build;
import android.os.Process;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;

import org.dragons.scoutingapp.R;

/**
 * Created by Spud on 9/12/21 for the project: AndroidApp.
 * <p>
 * For the license, view the file titled LICENSE at the root of the project.
 *
 * @version 1.0
 * @since DEVELOPMENT_VERSION.
 */
public class MainActivityViewModel extends AndroidViewModel {

	public MainActivityViewModel(Application application) {
		super(application);
	}

	/**
	 * Documentation
	 * Comments
	 *
	 * @param activity
	 * @return
	 */
	boolean checkPermissions(Activity activity) {

		String[] permissions;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			permissions = new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
					Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN};
		} else {
			permissions = new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN};
		}
		String[] potentialMissingPermissions = new String[permissions.length];
		int numberOfMissingPermissions = 0;
		for (String permission : permissions) {
			if (this.getApplication().checkPermission(permission, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_DENIED) {
				Log.w("onResume", "Missing permission " + permission);
				potentialMissingPermissions[numberOfMissingPermissions] = permission;
				numberOfMissingPermissions++;
			}
		}

		if (numberOfMissingPermissions > 0) {
			String[] missingPermissions = new String[numberOfMissingPermissions];
			System.arraycopy(potentialMissingPermissions, 0, missingPermissions, 0, numberOfMissingPermissions);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				activity.requestPermissions(missingPermissions, this.hashCode());
			}
			return false;
		}

		return true;
	}

}
