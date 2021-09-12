package org.dragons.scoutingapp.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.zxing.Result;

import org.dragons.scoutingapp.R;
import org.dragons.scoutingapp.bluefiles.BlueThread;
import org.dragons.scoutingapp.databinding.MacaddressBinding;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Stephen Ogden on 9/11/21.
 * FRC 1595
 */
public class EnterMACAddress extends Activity implements ZXingScannerView.ResultHandler {

	private Animation shake;

	@Override
	protected void onCreate(@Nullable Bundle savedInstance) {
		super.onCreate(savedInstance);

		final MacaddressBinding binder = DataBindingUtil.setContentView(this, R.layout.macaddress);

		this.shake = AnimationUtils.loadAnimation(this, R.anim.skake);

		binder.connect.setOnClickListener(view -> {
			String macAddress = binder.macAddressInput.getText().toString().toUpperCase(java.util.Locale.US);

			if (BluetoothAdapter.checkBluetoothAddress(macAddress)) {
				BlueThread.INSTANCE.start(macAddress);
				this.finish();
			} else {
				binder.macAddressInput.startAnimation(this.shake);
			}
		});

		binder.scan.setOnClickListener(view -> {

			// TODO Test for camera permissions

			ZXingScannerView scannerView = new ZXingScannerView(this);
			scannerView.setResultHandler(this);
			scannerView.startCamera();
		});

		binder.cancel.setOnClickListener(view -> this.finish());
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.finish();
	}

	@Override
	public void handleResult(Result result) {
		// TODO
	}
}
