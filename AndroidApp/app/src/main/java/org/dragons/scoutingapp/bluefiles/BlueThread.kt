package org.dragons.scoutingapp.bluefiles

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import org.dragons.scoutingapp.MatchFiles.MatchData
import org.dragons.scoutingapp.R
import org.json.JSONObject
import org.dragons.scoutingapp.bluefiles.BlueThreadRequest.Requests
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.util.*
import kotlin.Throws

/**
 * Created by Stephen Ogden on 1/16/19.
 * FRC 1595
 */
object BlueThread : Thread() {

	/**
	 * Documentation
	 */
	lateinit var remoteDeviceName: String
		private set

	/**
	 * Documentation
	 */
	var running: Boolean = false
		private set

	/**
	 * Documentation
	 */
	var autonomous: MatchData? = null
		private set

	/**
	 * Documentation
	 */
	var teleOp: MatchData? = null
		private set

	/**
	 * Documentation
	 */
	var endgame: MatchData? = null
		private set

	/**
	 * Documentation
	 */
	private lateinit var MACAddress: String

	/**
	 * Documentation
	 */
	private var bluetoothSocket: BluetoothSocket? = null

	/**
	 * Documentation
	 */
	private var input: BufferedReader? = null

	/**
	 * Documentation
	 */
	private var output: OutputStream? = null

	/**
	 * Documentation
	 * @param MACAddress
	 */
	fun start(MACAddress: String, activity: Activity) {

		this.MACAddress = MACAddress

		when (this.MACAddress) {
			activity.getString(R.string.debug_connected) -> {
				this.remoteDeviceName = "DEBUG SERVER"
				this.running = true
			}
			activity.getString(R.string.debug_match_data) -> {
				// TODO hard code name, match data, and running.
				this.remoteDeviceName = "DEBUG SERVER"
				this.setMatchData(JSONObject("{ \"Autonomous\" : { \"Crosses HAB line\" : " +
						"[\"Boolean\", false], \"Number of hatch panels placed on cargo ship\" : " +
						"[\"Number\", 0, 0, 10, 1], \"Number of hatch panels placed on rocket\" : " +
						"[\"Number\", 0, 0, 6, 1], \"Number of hatch panels dropped\" : " +
						"[\"Number\", 0, 0, 20, 1], \"Number of cargo placed in cargo ship\" : " +
						"[\"Number\", 0, 0, 10, 1], \"Number of cargo placed in rocket\" : " +
						"[\"Number\", 0,0,6,1], \"Number of cargo dropped/popped\" : " +
						"[\"Number\", 0, 0, 20, 1] }, \"TeleOp\" : " +
						"{ \"Number of hatch panels placed on cargo ship\" : " +
						"[\"Number\", 0, 0, 10, 1], \"Number of hatch panels placed on rocket\" : " +
						"[\"Number\", 0, 0, 6, 1], \"Number of hatch panels dropped\" : " +
						"[\"Number\", 0, 0, 20, 1], \"Number of cargo placed in cargo ship\" : " +
						"[\"Number\", 0, 0, 10, 1], \"Number of cargo placed in rocket\" : " +
						"[\"Number\", 0,0,6,1], \"Number of cargo dropped/popped\" : " +
						"[\"Number\", 0, 0, 20, 1] }, \"Endgame\" : { \"HAB Actions\" : " +
						"[\"BooleanGroup\", { \"Robot doesn\'t return to HAB\" : true, " +
						"\"Robot climbs on level 1\" : false, \"Robot climbs on level 2\" : " +
						"false, \"Robot climbs on level 3\" : false }], " +
						"\"Assists 1 other robot in climb\": [\"Boolean\", false], " +
						"\"Assists 2 other robots in climb\": [\"Boolean\", false] } }"))
				this.running = true
			}
			else -> {

				val bluetoothManager: BluetoothManager =
					activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

				val remoteDevice: BluetoothDevice =
					bluetoothManager.adapter.getRemoteDevice(this.MACAddress)

				// Well known SPP UUID
				this.bluetoothSocket = remoteDevice.
				createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))

				// Make sure that discovery is off, as its fairly resource intensive
				bluetoothManager.adapter.cancelDiscovery()

				this.bluetoothSocket!!.connect()
				this.remoteDeviceName = this.bluetoothSocket!!.remoteDevice.name
				this.input = BufferedReader(InputStreamReader(this.bluetoothSocket!!.inputStream))
				this.output = this.bluetoothSocket!!.outputStream

				this.start()
			}
		}
	}

	/**
	 * Documentation
	 * Comments
	 */
	override fun run() {
		Log.i("Bluethread", "Running bluethread...")
		this.running = true
		while (this.bluetoothSocket!!.isConnected) {

			val readInput: String = this.readInput()

			if (readInput != "") {
				try {

					// Parse the input to a Json object
					val jsonObject = JSONObject(readInput)

					Log.d("Received object", jsonObject.toString())
					when (this.parseRequest(jsonObject)) {
						Requests.REQUEST_CLOSE -> this.close(false)
						Requests.CONFIG -> {
							val config: JSONObject = jsonObject.getJSONObject(Requests.CONFIG.name)
							this.setMatchData(config)
						}
						Requests.REQUEST_PING -> this.requestPing()
						Requests.DATA -> {
							Log.d("Validated object", "Its data!")
							Log.d("Data-dump", jsonObject.optString(Requests.DATA.name))
						}
					}
				} catch (e: JSONException) {
					Log.e("BlueThread", "Json is invalid: $readInput", e)
				} catch (IOException: IOException) {
					// TODO
				}
			}

			yield()
		}

		// Be sure to close the socket
		try {
			this.close(false)
		} catch (e: IOException) {
			if (e.toString() != "java.io.IOException: socket closed") {
				e.printStackTrace()
			}
		}

		Log.i("Bluethread", "Stopping bluethread...")
		this.MACAddress = ""
		this.running = false
	}

	/**
	 * Documentation
	 * Comments
	 */
	private fun requestPing() {
		Log.d("Validated object", "Requested ping")

		// Get the time in MS since midnight
		val c = Calendar.getInstance()
		val now = c.timeInMillis
		c[Calendar.HOUR_OF_DAY] = 0
		c[Calendar.MINUTE] = 0
		c[Calendar.SECOND] = 0
		c[Calendar.MILLISECOND] = 0
		val passed = now - c.timeInMillis
		val secondsPassed = passed / 1000
		val data = JSONObject(String.format("{\"Time\":%s}", secondsPassed))
		this.sendData(
			BlueThreadRequest(
				Requests.REQUEST_PING,
				data
			)
		)
	}

	/**
	 * Documentation
	 * Comments
	 * @return
	 */
	private fun readInput(): String {

		var line = ""
		try {
			if (this.input!!.ready()) {
				line = this.input!!.readLine()
				Log.d("readInput","Read input: $line")
			}
		} catch (IOException: IOException) {
			Log.e("readInput", "IO Exception thrown while reading input.", IOException)
		}

		return line
	}

	/**
	 * Documentation
	 *
	 * @param json
	 */
	@Throws(JSONException::class)
	private fun setMatchData(json: JSONObject) {
		Log.d("fullData", json.toString())

		// Get the autonomous stuff
		val rawAutonomous: JSONObject = json.getJSONObject("Autonomous")
		val autoSize = rawAutonomous.length()
		Log.d("rawAutonomous", rawAutonomous.toString())
		Log.d("rawAutonomousSize", autoSize.toString())
		this.autonomous = MatchData(rawAutonomous, autoSize)

		// Get the teleop stuff
		val rawTeleOp: JSONObject = json.getJSONObject("TeleOp")
		val teleSize = rawTeleOp.length()
		Log.d("rawTeleOp", rawTeleOp.toString())
		Log.d("teleSize", teleSize.toString())
		this.teleOp = MatchData(rawTeleOp, teleSize)

		// Get the endgame stuff
		val rawEndgame:JSONObject = json.getJSONObject("Endgame")
		val endSize = rawEndgame.length()
		Log.d("rawEndgame", rawEndgame.toString())
		Log.d("endSize", endSize.toString())
		this.endgame = MatchData(rawEndgame, endSize)
	}

	/**
	 * Parses a JSON request in order to determine what request was delivered by the server.
	 *
	 * @param input The JSON to parse.
	 * @return The Request send by the server.
	 */
	private fun parseRequest(input: JSONObject): Requests {
		return when {
			input.has(Requests.REQUEST_CLOSE.name) -> Requests.REQUEST_CLOSE
			input.has(Requests.REQUEST_PING.name) -> Requests.REQUEST_PING
			input.has(Requests.CONFIG.name) -> Requests.CONFIG
			else -> Requests.DATA
		}
	}

	/**
	 * Close the socket/thread.
	 * Documentation
	 * Comments
	 * @param isRequest Whether or not to write the close request to the stream.
	 */
	fun close(isRequest: Boolean) {
		Log.i("Bluethread.close", "Closing bluethread...")
		if (isRequest) { this.sendData(BlueThreadRequest(Requests.REQUEST_CLOSE, null)) }
		try {
			if (this.output != null) {
				this.output!!.flush()
				this.output!!.close()
			}
			if (this.input != null) { this.input!!.close() }
			if (this.bluetoothSocket != null) { this.bluetoothSocket!!.close() }
			this.autonomous = null
			this.teleOp = null
			this.endgame = null
			this.running = false
		} catch (IOException : IOException) {
			Log.e("BlueThread.close", "Unable to close bluethread", IOException)
		}
	}

	/**
	 * Sends data to the server.
	 *
	 * @param blueThreadRequest The data request.
	 */
	fun sendData(blueThreadRequest: BlueThreadRequest) {
		Log.v("sendData", "Sending data...")

		// Check for nulls in the data
		blueThreadRequest.data = if (blueThreadRequest.data == null) JSONObject().also { blueThreadRequest.data = it } else blueThreadRequest.data
		try {
			if (this.output == null) {
				return
			}
			this.output!!.write("{\"${blueThreadRequest.requests.name}\":${blueThreadRequest.data}}\n".toByteArray())
			this.output!!.flush()
		} catch (e: IOException) {
			if (e.toString() == "java.io.IOException: socket closed" || e.toString() == "java.io.IOException: Broken pipe") {
				Log.w("sendData", "Unable to send data as the connection was closed.")
			} else {
				Log.e("sendData", "Unable to send data", e)
			}
		}
	}
}