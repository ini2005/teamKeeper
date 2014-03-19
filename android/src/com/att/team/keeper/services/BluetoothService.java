package com.att.team.keeper.services;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;

public enum BluetoothService {

	INSTANCE;

	static final String TAG = "BluetoothService";

	/**
	 * get bluetooth local device name
	 * 
	 * @return device name String
	 */
	public String getLocalBluetoothName() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();

		// if device does not support Bluetooth
		if (mBluetoothAdapter == null) {
			Log.d(TAG, "device does not support bluetooth");
			return null;
		}

		return mBluetoothAdapter.getName();
	}

	/**
	 * get bluetooth adapter MAC address
	 * 
	 * @return MAC address String
	 */
	public String getBluetoothMacAddress() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();

		// if device does not support Bluetooth
		if (mBluetoothAdapter == null) {
			Log.d(TAG, "device does not support bluetooth");
			return null;
		}

		return mBluetoothAdapter.getAddress();
	}

}
