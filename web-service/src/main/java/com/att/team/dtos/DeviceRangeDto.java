package com.att.team.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceRangeDto {

	@JsonProperty("bluetoothRssi")
	private int mBluetoothRssi;

	@JsonProperty("bluetoothMac")
	private String mBluetoothMac;

	public int getBluetoothRssi() {
		return mBluetoothRssi;
	}

	public void setBluetoothRssi(int bluetoothRssi) {
		mBluetoothRssi = bluetoothRssi;
	}

	public String getBluetoothMac() {
		return mBluetoothMac;
	}

	public void setBluetoothMac(String bluetoothMac) {
		mBluetoothMac = bluetoothMac;
	}

}
