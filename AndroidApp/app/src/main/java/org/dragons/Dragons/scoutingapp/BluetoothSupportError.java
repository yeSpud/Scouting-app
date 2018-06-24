package org.dragons.Dragons.scoutingapp;

final class BluetoothSupportError extends Exception {

    BluetoothSupportError() {
        super("Bluetooth is not supported on this device.");
    }
}
