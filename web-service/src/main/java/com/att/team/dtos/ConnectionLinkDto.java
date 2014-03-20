package com.att.team.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ConnectionLinkDto {


	@JsonProperty("source")
	private int mSource;
	
	@JsonProperty("target")
	private int mTarget;
	
	@JsonProperty("value")
	private int mValue;

	public int getSource() {
		return mSource;
	}

	public void setSource(int source) {
		mSource = source;
	}

	public int getTarget() {
		return mTarget;
	}

	public void setTarget(int target) {
		mTarget = target;
	}

	public int getValue() {
		return mValue;
	}

	public void setValue(int value) {
		mValue = value;
	}

	
	
	
	
	
	
}
