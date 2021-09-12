package org.dragons.scoutingapp.bluefiles

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import org.dragons.scoutingapp.MatchFiles.Match
import org.json.JSONObject
import org.dragons.scoutingapp.bluefiles.Request.Requests
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.util.*
import kotlin.Throws
import kotlin.system.exitProcess

/**
 * Created by Stephen Ogden on 1/16/19.
 * FRC 1595
 */
object BlueThread : Thread() {

	/**
	 * Documentation
	 */
	lateinit var MACAddress: String
		private set

	/**
	 * Documentation
	 */
	private lateinit var bluetoothSocket: BluetoothSocket

	/**
	 * Documentation
	 */
	lateinit var remoteDeviceName: String
		private set

	/**
	 * Documentation
	 */
	private lateinit var input: BufferedReader

	/**
	 * Documentation
	 */
	private lateinit var output: OutputStream

	/**
	 * Documentation
	 */
	var running: Boolean = false
		private set

	/**
	 * Documentation
	 */
	var hasMatchData: Boolean = false

	/**
	 * Documentation
	 */
	val matchData: Match = Match()

	/**
	 * Documentation
	 * @param MACAddress
	 */
	fun start(MACAddress: String) {

		this.MACAddress = MACAddress

		val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

		val remoteDevice: BluetoothDevice = bluetoothAdapter.getRemoteDevice(this.MACAddress)

		// Well known SPP UUID
		this.bluetoothSocket = remoteDevice.createRfcommSocketToServiceRecord(UUID.
		fromString("00001101-0000-1000-8000-00805F9B34FB"))

		// Make sure that discovery is off, as its fairly resource intensive
		bluetoothAdapter.cancelDiscovery()

		this.bluetoothSocket.connect()
		this.remoteDeviceName = this.bluetoothSocket.remoteDevice.name
		this.input = BufferedReader(InputStreamReader(this.bluetoothSocket.inputStream))
		this.output = this.bluetoothSocket.outputStream

		this.start()
	}

	/**
	 * Documentation
	 * Comments
	 */
	override fun run() {
		Log.i("Bluethread", "Running bluethread...")
		this.running = true
		while (this.bluetoothSocket.isConnected) {

			val readInput: String = this.readInput()

			if (readInput != "") {

				// Parse the input to a Json object
				val jsonObject: JSONObject = try {
					JSONObject(readInput)
				} catch (e: JSONException) {
					Log.e("BlueThread", "Json is invalid: $readInput", e)
					continue
				}

				Log.d("Received object", jsonObject.toString())
				when (this.parseRequest(jsonObject)) {
					Requests.REQUEST_CLOSE -> this.requestClosed()
					Requests.CONFIG -> this.requestConfig(jsonObject)
					Requests.REQUEST_PING -> this.requestPing()
					Requests.DATA -> {
						Log.d("Validated object", "Its data!")
						Log.d("Data-dump", jsonObject.optString(Requests.DATA.name))
					}
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
		this.sendData(Request(Requests.REQUEST_PING, data))
	}

	/**
	 * Documentation
	 * Comments
	 *
	 * @param jsonObject
	 */
	private fun requestConfig(jsonObject: JSONObject) {
		Log.d("Validated object", "Config")
		try {
			val config = jsonObject.getJSONObject(Requests.CONFIG.name)
			this.setMatchData(config)
			this.hasMatchData = true
		} catch (JSONException: JSONException) {
			JSONException.printStackTrace()
		}
	}

	/**
	 * Documentation
	 * Comments
	 */
	private fun requestClosed() {
		Log.d("Validated object", "Requested closure")
		try {
			this.close(false)
		} catch (e: IOException) {
			e.printStackTrace()
		}
	}

	/**
	 * Documentation
	 * Comments
	 * @return
	 */
	private fun readInput(): String {

		var line = ""

		try {

			if (this.input.ready()) {
				line = this.input.readLine()
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
	fun setMatchData(json: JSONObject) {
		Log.d("fullData", json.toString())

		// Get the autonomous stuff
		val rawAutonomous = json.optJSONObject("Autonomous")
		val autoSize = rawAutonomous.length() ?: 0
		Log.d("rawAutonomous", rawAutonomous.toString())
		Log.d("rawAutonomousSize", autoSize.toString())
		this.matchData.autonomousData = Match.matchBaseToAutonomous(Match.getMatchData(rawAutonomous, autoSize))

		// Get the teleop stuff
		val rawTeleOp = json.optJSONObject("TeleOp")
		val teleSize = rawTeleOp.length()
		Log.d("rawTeleOp", rawTeleOp.toString())
		Log.d("teleSize", teleSize.toString())
		this.matchData.teleopData = Match.matchBaseToTeleOp(Match.getMatchData(rawTeleOp, teleSize))

		// Get the endgame stuff
		val rawEndgame = json.optJSONObject("Endgame")
		val endSize = rawEndgame.length()
		Log.d("rawEndgame", rawEndgame.toString())
		Log.d("endSize", endSize.toString())
		this.matchData.endgameData = Match.matchBaseToEndgame(Match.getMatchData(rawEndgame, endSize))
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
	 *
	 * @param isRequest Whether or not to write the close request to the stream.
	 * @throws IOException For when something goes wrong...
	 */
	@Throws(IOException::class)
	fun close(isRequest: Boolean) {
		Log.d("Bluethread", "Closing")
		if (isRequest) {
			this.sendData(Request(Requests.REQUEST_CLOSE, null))
		}
		this.output.flush()
		this.output.close()
		this.input.close()
		this.bluetoothSocket.close()
	}

	/**
	 * Sends data to the server.
	 *
	 * @param request The data request.
	 */
	fun sendData(request: Request) {
		Log.d("Bluethread", "Sending data")

		// Check for nulls in the data
		request.data = if (request.data == null) JSONObject().also { request.data = it } else request.data
		try {
			this.output.write("{\"${request.requests.name}\":${request.data}}\n".toByteArray())
			this.output.flush()
		} catch (e: IOException) {
			if (e.toString() != "java.io.IOException: socket closed" || e.toString() != "java.io.IOException: Broken pipe") { // FIXME
				e.printStackTrace()
			}
		}
	}
}