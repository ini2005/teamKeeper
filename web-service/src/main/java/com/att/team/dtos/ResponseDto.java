package com.att.team.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseDto {

	@JsonProperty("members")
	private List<MemberDto> mMembers;

	@JsonProperty("groups")
	private List<List<MemberDto>> mGroups;

	
	
	public List<MemberDto> getMembers() {
		return mMembers;
	}

	public void setMembers(List<MemberDto> members) {
		mMembers = members;
	}

	public List<List<MemberDto>> getGroups() {
		return mGroups;
	}

	public void setGroups(List<List<MemberDto>> groups) {
		mGroups = groups;
	}

}
