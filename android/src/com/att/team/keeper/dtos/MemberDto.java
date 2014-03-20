package com.att.team.keeper.dtos;

import java.util.List;

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
	private long mLastUpdateTime;
	
	@JsonProperty("imageUrl")
	private long mImageUrl;

	@JsonProperty("panic")
	private String mPanic;

	@JsonProperty("lastSeenBy")
	private List<MemberDto> mLastSeenBy;
	
	
	
	


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

	public long getLastUpdateTime() {
		return mLastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		mLastUpdateTime = lastUpdateTime;
	}

	public long getImageUrl() {
		return mImageUrl;
	}

	public void setImageUrl(long imageUrl) {
		mImageUrl = imageUrl;
	}

	public String getPanic() {
		return mPanic;
	}

	public void setPanic(String panic) {
		mPanic = panic;
	}

	public List<MemberDto> getLastSeenBy() {
		return mLastSeenBy;
	}

	public void setLastSeenBy(List<MemberDto> lastSeenBy) {
		mLastSeenBy = lastSeenBy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mBluetoothMac == null) ? 0 : mBluetoothMac.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MemberDto other = (MemberDto) obj;
		if (mBluetoothMac == null) {
			if (other.mBluetoothMac != null)
				return false;
		} else if (!mBluetoothMac.equals(other.mBluetoothMac))
			return false;
		return true;
	}
}
