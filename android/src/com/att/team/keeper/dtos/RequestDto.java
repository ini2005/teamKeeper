package com.att.team.keeper.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestDto {

	@JsonProperty("memberDto")
	private MemberDto mMemberDto;

	@JsonProperty("devicesInRange")
	List<DeviceRangeDto> mDevicesInRange;

	public MemberDto getMemberDto() {
		return mMemberDto;
	}

	public void setMemberDto(MemberDto memberDto) {
		mMemberDto = memberDto;
	}

	public List<DeviceRangeDto> getDevicesInRange() {
		return mDevicesInRange;
	}

	public void setDevicesInRange(List<DeviceRangeDto> devicesInRange) {
		mDevicesInRange = devicesInRange;
	}

}
