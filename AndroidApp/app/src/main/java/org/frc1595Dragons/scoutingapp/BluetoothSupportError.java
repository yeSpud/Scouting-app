package org.frc1595Dragons.scoutingapp;

/**
 * Created by Stephen Ogden on 7/17/2018.
 * FTC 6128 | 7935
 * FRC 1595
 */

// Error thrown when the device in question does not support bluetooth (like and emulator)
final class BluetoothSupportError extends Exception {
    BluetoothSupportError() {
        super("Bluetooth is not supported on this device.");
    }
}
