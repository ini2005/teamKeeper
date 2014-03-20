package com.att.team.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;


public class DeviceRange {

	@JsonProperty("bluetoothRssi")
	private String mBluetoothRssi;

	@JsonProperty("bluetoothMac")
	private String mBluetoothMac;

	public String getBluetoothRssi() {
		return mBluetoothRssi;
	}

	public void setBluetoothRssi(String bluetoothRssi) {
		mBluetoothRssi = bluetoothRssi;
	}

	public String getBluetoothMac() {
		return mBluetoothMac;
	}

	public void setBluetoothMac(String bluetoothMac) {
		mBluetoothMac = bluetoothMac;
	}

}
