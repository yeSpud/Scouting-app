package org.dragons.scoutingapp.activities

import android.Manifest
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.app.Activity
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Build
import android.content.pm.PackageManager
import android.os.Process
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.dragons.scoutingapp.bluefiles.BlueThread

/**
 * Created by Stephen Ogden on 9/12/21.
 * FRC 1595.
 */
class MainActivityViewModel : ViewModel() {

	/**
	 * Documentation
	 */
	private val _matchDataReady: MutableLiveData<Boolean> = MutableLiveData() // TODO Add listener for when match data is ready!

	/**
	 * Documentation
	 */
	val matchDataReady: LiveData<Boolean>
		get() = this._matchDataReady

	/**
	 * Documentation
	 */
	private val _connected: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

	/**
	 * Documentation
	 *
	 * @return
	 */
	val connected: LiveData<Boolean>
		get() = this._connected

	/**
	 * Documentation
	 */
	private val _readyToConnect: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

	/**
	 * Documentation
	 *
	 * @return
	 */
	val readyToConnect : LiveData<Boolean>
		get() = this._readyToConnect

	/**
	 * Documentation
	 * Comments
	 *
	 * @param activity
	 * @return
	 */
	fun setupCoroutine(activity: MainActivity): Job {

		return viewModelScope.launch(context = Dispatchers.Main, start = CoroutineStart.LAZY) {
			Log.v("setupCoroutine", "Started coroutine")

			activity.log("Checking permissions...")
			if (!checkPermissions(activity)) {
				activity.log("Missing critical permissions (probably bluetooth)")
				return@launch
			}
			activity.log("Correct permissions found")

			activity.log("Checking if bluetooth is enabled...")
			val bluetoothManager: BluetoothManager = activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
			if (!bluetoothManager.adapter.isEnabled) {
				activity.log("Bluetooth is disabled. Asking user to turn on bluetooth...")
				val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
				activity.startActivityForResult(enableBtIntent, BluetoothAdapter.STATE_ON)
				return@launch
			}
			activity.log("Bluetooth is enabled")

			activity.log("Checking if already connected to device...")
			if (BlueThread.running) {
				activity.log("Connected to ${BlueThread.remoteDeviceName}")
				this@MainActivityViewModel._connected.value = true

				activity.log("Checking matchdata...")
				if (BlueThread.autonomous != null || BlueThread.teleOp != null || BlueThread.endgame != null) {
					activity.log("Ready to scout!")
					this@MainActivityViewModel._matchDataReady.value = true
				} else {
					activity.log("Awaiting match data...")
					this@MainActivityViewModel._matchDataReady.value = false
				}
			} else {
				activity.log("Not yet connected")
				this@MainActivityViewModel._connected.value = false
			}
			this@MainActivityViewModel._readyToConnect.value = true

			Log.v("setupCoroutine", "Finished coroutine")
		}
	}

	/**
	 * Documentation
	 * Comments
	 *
	 * @param activity
	 * @return
	 */
	private fun checkPermissions(activity: Activity): Boolean {
		val permissions: Array<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
				Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN)
		} else {
			arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN)
		}

		val potentialMissingPermissions = arrayOfNulls<String>(permissions.size)
		var numberOfMissingPermissions = 0
		for (permission in permissions) {
			if (activity.checkPermission(permission, Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_DENIED) {

				Log.w("onResume", "Missing permission $permission")
				potentialMissingPermissions[numberOfMissingPermissions] = permission
				numberOfMissingPermissions++
			}
		}

		if (numberOfMissingPermissions > 0) {
			val missingPermissions = arrayOfNulls<String>(numberOfMissingPermissions)
			System.arraycopy(potentialMissingPermissions, 0, missingPermissions, 0, numberOfMissingPermissions)
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				activity.requestPermissions(missingPermissions, this.hashCode())
			}
			return false
		}
		return true
	}

	/**
	 * Documentation
	 */
	fun disconnect(activity: MainActivity) {
		activity.log("Disconnecting from ${BlueThread.remoteDeviceName}")
		BlueThread.close(true)
		this._matchDataReady.value = false
		this._connected.value = false
	}
}