package org.dragons.scoutingapp.activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import org.dragons.scoutingapp.BuildConfig;
import org.dragons.scoutingapp.R;
import org.dragons.scoutingapp.bluefiles.BlueThread;
import org.dragons.scoutingapp.databinding.MacaddressBinding;

import java.util.Collections;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Stephen Ogden on 9/11/21.
 * FRC 1595
 */
public class EnterMACAddress extends AppCompatActivity implements ZXingScannerView.ResultHandler {

	/**
	 * Documentation
	 */
	private MacaddressBinding binder;

	/**
	 * Documentation
	 */
	private Animation shake;

	@Override
	protected void onCreate(@Nullable Bundle savedInstance) {
		super.onCreate(savedInstance);

		this.binder = DataBindingUtil.setContentView(this, R.layout.macaddress);
		this.setTitle(R.string.enter_mac_address);

		this.shake = AnimationUtils.loadAnimation(this, R.anim.skake);

		this.binder.debugConnected.setOnClickListener(view -> {
			BlueThread.INSTANCE.start(this.getResources().getString(R.string.debug_connected), this);
			this.finish();
		});

		this.binder.debugMatchData.setOnClickListener(view -> {
			BlueThread.INSTANCE.start(this.getResources().getString(R.string.debug_match_data), this);
			this.finish();
		});

		this.binder.connect.setOnClickListener(view -> {
			String macAddress = this.binder.macAddressInput.getText().toString().toUpperCase(Locale.US);

			if (BluetoothAdapter.checkBluetoothAddress(macAddress)) {
				BlueThread.INSTANCE.start(macAddress, this);
				this.finish();
			} else {
				this.binder.macAddressInput.startAnimation(this.shake);
			}
		});

		this.binder.scan.setOnClickListener(view -> {

			if (this.checkPermission(Manifest.permission.CAMERA, Process.myPid(), Process.myUid())
					== PackageManager.PERMISSION_DENIED) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					this.requestPermissions(new String[]{Manifest.permission.CAMERA}, this.hashCode());
				}
			} else {
				this.startQRCodeCamera();
			}
		});

		this.binder.cancel.setOnClickListener(view -> this.finish());
	}

	/**
	 * Documentation
	 */
	private void startQRCodeCamera() {
		this.binder.cameraView.setResultHandler(this);
		this.binder.cameraView.startCamera();
		this.binder.cameraView.setAutoFocus(true);
		this.binder.cameraView.setFormats(Collections.singletonList(BarcodeFormat.QR_CODE));
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.binder.cameraView.stopCameraPreview();
		this.binder.cameraView.stopCamera();
		this.finish();
	}

	@Override
	public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (requestCode == this.hashCode()) {
			if (permissions[0].equals(Manifest.permission.CAMERA)) {
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					this.startQRCodeCamera();
				}
			}
		}
	}

	@Override
	public void handleResult(@NonNull Result result) {
		this.binder.cameraView.stopCameraPreview();
		this.binder.cameraView.stopCamera();
		this.binder.macAddressInput.setText(result.getText());
	}
}
