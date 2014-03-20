package com.att.team.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ConnectionsDto {

	

	@JsonProperty("nodes")
	private List<ConnectionMemberDto> mNodes;
	
	@JsonProperty("Links")
	private List<ConnectionLinkDto> mLinks;

	public List<ConnectionMemberDto> getNodes() {
		return mNodes;
	}

	public void setNodes(List<ConnectionMemberDto> nodes) {
		mNodes = nodes;
	}

	public List<ConnectionLinkDto> getLinks() {
		return mLinks;
	}

	public void setLinks(List<ConnectionLinkDto> links) {
		mLinks = links;
	}
	
	
}
