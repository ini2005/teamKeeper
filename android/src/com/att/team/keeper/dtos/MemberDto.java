package com.att.team.keeper.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MemberDto {

	@JsonProperty("firstName")
	private String mFirstName;

	@JsonProperty("lastName")
	private String mLastName;

	@JsonProperty("bluetoothName")
	private String mBluetoothName;

	@JsonProperty("bluetoothMac")
	private String mBluetoothMac;

	@JsonProperty("mobileNumber")
	private String mMobileNumber;

	@JsonProperty("lastUpdateTime")
	private long mLastUpdatetime;

	@JsonProperty("panic")
	private String mPanic;

	public String getFirstName() {
		return mFirstName;
	}

	public void setFirstName(String firstName) {
		mFirstName = firstName;
	}

	public String getLastName() {
		return mLastName;
	}

	public void setLastName(String lastName) {
		mLastName = lastName;
	}

	public String getBluetoothName() {
		return mBluetoothName;
	}

	public void setBluetoothName(String bluetoothName) {
		mBluetoothName = bluetoothName;
	}

	public String getBluetoothMac() {
		return mBluetoothMac;
	}

	public void setBluetoothMac(String bluetoothMac) {
		mBluetoothMac = bluetoothMac;
	}

	public String getMobileNumber() {
		return mMobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		mMobileNumber = mobileNumber;
	}

	public long getLastUpdatetime() {
		return mLastUpdatetime;
	}

	public void setLastUpdatetime(long lastUpdatetime) {
		mLastUpdatetime = lastUpdatetime;
	}

	public String getPanic() {
		return mPanic;
	}

	public void setPanic(String panic) {
		mPanic = panic;
	}

}
