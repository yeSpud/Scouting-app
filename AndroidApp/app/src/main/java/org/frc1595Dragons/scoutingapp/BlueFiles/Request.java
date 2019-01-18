package org.frc1595Dragons.scoutingapp.BlueFiles;

import org.json.JSONObject;

/**
 * Created by Stephen Ogden on 1/16/19.
 * FTC 6128 | 7935
 * FRC 1595
 */

public class Request {
    Requests requests;
    JSONObject data;

    Request(Requests requests, JSONObject data) {
        this.requests = requests;
        this.data = data;
    }

    public enum Requests {
        DATA,
        CONFIG,
        REQUEST_PING,
        REQUEST_CLOSE
    }

}
