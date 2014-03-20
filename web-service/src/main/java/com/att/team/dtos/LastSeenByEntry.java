package com.att.team.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class LastSeenByEntry {

	

	@JsonProperty("name")
	private String mName;
	
	@JsonProperty("lastName")
	private String mLastName;
	
	@JsonProperty("phone")
	private String mPhone;
	
	@JsonProperty("photoUrl")
	private String mPhotoUrl;

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getLastName() {
		return mLastName;
	}

	public void setLastName(String lastName) {
		mLastName = lastName;
	}

	public String getPhone() {
		return mPhone;
	}

	public void setPhone(String phone) {
		mPhone = phone;
	}

	public String getPhotoUrl() {
		return mPhotoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		mPhotoUrl = photoUrl;
	}
	
	

	
}
