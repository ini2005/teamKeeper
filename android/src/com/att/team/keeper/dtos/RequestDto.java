package com.att.team.keeper.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestDto {

	@JsonProperty("firstName")
	private MemberDto mMemberDto;

	@JsonProperty("devicesInRange")
	List<DeviceRange> mDevicesInRange;

	public MemberDto getMemberDto() {
		return mMemberDto;
	}

	public void setMemberDto(MemberDto memberDto) {
		mMemberDto = memberDto;
	}

	public List<DeviceRange> getDevicesInRange() {
		return mDevicesInRange;
	}

	public void setDevicesInRange(List<DeviceRange> devicesInRange) {
		mDevicesInRange = devicesInRange;
	}

}
