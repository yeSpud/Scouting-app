package org.dragons.Dragons.scoutingapp;

/**
 * Created by Stephen Ogden on 10/8/18.
 * FTC 6128 | 7935
 * FRC 1595
 */
final class BluetoothSendError extends Exception {
    BluetoothSendError() {
     super("There was an issue sending data to the receiving device.");
    }
}
