package com.att.team.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CircleChildDto {

	

	@JsonProperty("name")
	private String mName;
	
	@JsonProperty("size")
	private int mSize;
	
	@JsonProperty("children")
	List<CircleChildDto> mChildren;

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public int getSize() {
		return mSize;
	}

	public void setSize(int size) {
		mSize = size;
	}

	public List<CircleChildDto> getChildren() {
		return mChildren;
	}

	public void setChildren(List<CircleChildDto> children) {
		mChildren = children;
	}

	

	
}
