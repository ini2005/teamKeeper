package com.att.team.keeper.requests;

import java.util.Map;

import org.springframework.http.HttpMethod;

import com.att.team.keeper.dtos.RequestDto;
import com.att.team.keeper.dtos.ResponseDto;

public class DeviceInRangeReqeust  extends BaseRequest<RequestDto, ResponseDto> {

	public DeviceInRangeReqeust(NetworkThread networkThread) {
		super(networkThread);
	}

	private RequestDto mRequestDto;

	@Override
	public HttpMethod getMethod() {
		return HttpMethod.POST;
	}

	@Override
	public String getUrl() {
		return "http://10.114.20.49:8080/team/v1/agent/deviceInRange";
	}

	@Override
	public Map<String, String> getHeaders() {
		return null;
	}

	@Override
	public RequestDto getRequestBody() {
		return mRequestDto;
	}

	@Override
	public Class<ResponseDto> getResponseClass() {
		return ResponseDto.class;
	}

	@Override
	protected boolean checkForLatestConfigVersion() {
		return false;
	}

	public void setRequestDto(RequestDto requestDto) {
		mRequestDto = requestDto;

	}

}
