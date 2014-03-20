package com.att.team.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ConnectionMemberDto {

	

	@JsonProperty("name")
	private String mName;
	
	
	@JsonProperty("group")
	private int mGroup;


	public String getName() {
		return mName;
	}


	public void setName(String name) {
		mName = name;
	}


	public int getGroup() {
		return mGroup;
	}


	public void setGroup(int group) {
		mGroup = group;
	}
	
	
}
