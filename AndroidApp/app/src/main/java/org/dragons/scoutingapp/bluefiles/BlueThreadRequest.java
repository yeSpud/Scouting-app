package org.dragons.scoutingapp.bluefiles;

import org.json.JSONObject;

/**
 * Created by Stephen Ogden on 1/16/19.
 * FRC 1595
 */

public class BlueThreadRequest {
	Requests requests;
	JSONObject data;

	/**
	 * Construct a new request.
	 *
	 * @param requests The type of request to be sent.
	 * @param data     The JSON data to be send with the request.
	 */
	@org.jetbrains.annotations.Contract(pure = true)
	public BlueThreadRequest(Requests requests, JSONObject data) {
		this.requests = requests;
		this.data = data;
	}

	/**
	 * Valid request options to be sent.
	 */
	public enum Requests {
		DATA,
		CONFIG,
		REQUEST_PING,
		REQUEST_CLOSE
	}

}
