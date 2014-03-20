package com.att.team.dtos;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseDto {

	@JsonProperty("members")
	private List<MemberDto> mMembers;

	@JsonProperty("groups")
	private List<Set<MemberDto>> mGroups;

	
	
	public List<MemberDto> getMembers() {
		return mMembers;
	}

	public void setMembers(List<MemberDto> members) {
		mMembers = members;
	}

	public List<Set<MemberDto>> getGroups() {
		return mGroups;
	}

	public void setGroups(List<Set<MemberDto>> groups) {
		mGroups = groups;
	}

}
