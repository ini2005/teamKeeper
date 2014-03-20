package com.att.team.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CirclesDto {

	

	@JsonProperty("name")
	private String mGroupName;
	
	@JsonProperty("children")
	List<CircleChildDto> mChildren;

	public String getmGroupName() {
		return mGroupName;
	}

	public void setGroupName(String mGroupName) {
		this.mGroupName = mGroupName;
	}

	public List<CircleChildDto> getChildren() {
		return mChildren;
	}

	public void setChildren(List<CircleChildDto> mChildren) {
		this.mChildren = mChildren;
	}
	
	

	
}
