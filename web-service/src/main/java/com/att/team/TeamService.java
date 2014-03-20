package com.att.team;

import org.jgrapht.graph.SimpleDirectedGraph;
import org.springframework.stereotype.Service;

import com.att.team.dtos.MemberDto;

@Service
public class TeamService {

	SimpleDirectedGraph<MemberDto, String> mRoomGraph;
}
