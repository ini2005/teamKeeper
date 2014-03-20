package com.att.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.AsUndirectedGraph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.springframework.stereotype.Service;

import com.att.team.dtos.CircleChildDto;
import com.att.team.dtos.CirclesDto;
import com.att.team.dtos.ConnectionLinkDto;
import com.att.team.dtos.ConnectionMemberDto;
import com.att.team.dtos.ConnectionsDto;
import com.att.team.dtos.DeviceRangeDto;
import com.att.team.dtos.MemberDto;
import com.att.team.dtos.RequestDto;
import com.att.team.dtos.ResponseDto;

@Service
public class TeamService {

	SimpleDirectedGraph<MemberDto, String> mRoomGraph;
	
	Map<String, MemberDto> mMembers = new HashMap<String, MemberDto>();
	
	public ResponseDto memberDataReceived(RequestDto requestDto){
		
		MemberDto memberDto = requestDto.getMemberDto();		
		memberDto.setLastUpdateTime(System.currentTimeMillis());
		
		if(mMembers.containsKey(memberDto.getBluetoothMac())){
			
			mMembers.get(memberDto.getBluetoothMac()).setLastUpdateTime(System.currentTimeMillis());
		
		}else{
			
			
			mMembers.put(memberDto.getBluetoothMac(), memberDto);
		}

		List<DeviceRangeDto> devicesInRange = requestDto.getDevicesInRange();
		
		updateMemberInGraph(memberDto, devicesInRange);
		
		ResponseDto responseDto = new ResponseDto();
		
		responseDto.setMembers(new ArrayList<MemberDto>(mMembers.values()));
		responseDto.setGroups(getSubgroups());
		
		return responseDto;
		
	}
	
	
	public CirclesDto getCirclesDto(){
		
		List<Set<MemberDto>> subgroups = getSubgroups();
		
		CirclesDto circlesDto = new CirclesDto();
		circlesDto.setGroupName("all");
		
		List<CircleChildDto> children = new ArrayList<CircleChildDto>();
		circlesDto.setChildren(children);
		
		int index = 0;
		for (Set<MemberDto> subGroup : subgroups) {
			
			CircleChildDto newGroup = new CircleChildDto();
			newGroup.setName("Group-" + index++);
			
			List<CircleChildDto> newGroupMembers = new ArrayList<CircleChildDto>();
			newGroup.setChildren(newGroupMembers);
			
			for (MemberDto memberDto : subGroup) {
				
				CircleChildDto curMember = new CircleChildDto();
				curMember.setName(memberDto.getFirstName() + " " + memberDto.getLastName());
				curMember.setSize(1000);
				newGroupMembers.add(curMember);
			}
			
			children.add(newGroup);
		}
		
		return circlesDto;
	}
	
	
	
	public ConnectionsDto getConnectionsDto(){
		
		String[] indexes = new String[mMembers.size()];
		
		
		
		ConnectionsDto connectionsDto = new ConnectionsDto();
		
		List<ConnectionMemberDto> connectionMemberDtos = new ArrayList<ConnectionMemberDto>();
		List<ConnectionLinkDto> connectionLinkDtos = new ArrayList<ConnectionLinkDto>();
		
		connectionsDto.setNodes(connectionMemberDtos);
		connectionsDto.setLinks(connectionLinkDtos);
		
		List<Set<MemberDto>> subgroups = getSubgroups();
		
		int membersIndex = 0;
		int groupIndex = 0;
		for (Set<MemberDto> group : subgroups) {
			groupIndex++;
			
			for (MemberDto memberDto : group) {
				
				indexes[membersIndex++] = memberDto.getBluetoothMac();
				ConnectionMemberDto curMemberDto = new ConnectionMemberDto();
				curMemberDto.setGroup(groupIndex);
				curMemberDto.setName(memberDto.getFirstName() + " " + memberDto.getLastName());
				connectionMemberDtos.add(curMemberDto);
			}
		}
		

		//generate links
		for(int i = 0; i < indexes.length - 1; i++){
			
			for(int j = 1; j < indexes.length; j++){
				
				String edgeNameDir1 = indexes[i] + "##" + indexes[j];
				String edgeNameDir2 = indexes[j] + "##" + indexes[i];
				
				if(mRoomGraph.containsEdge(edgeNameDir1)){
					
					ConnectionLinkDto connectionLinkDto = new ConnectionLinkDto();
					connectionLinkDto.setSource(i);
					connectionLinkDto.setTarget(j);
					connectionLinkDto.setValue(i + j);
					
					connectionLinkDtos.add(connectionLinkDto);
				
				}else if(mRoomGraph.containsEdge(edgeNameDir2)){
					
					ConnectionLinkDto connectionLinkDto = new ConnectionLinkDto();
					connectionLinkDto.setSource(j);
					connectionLinkDto.setTarget(i);
					connectionLinkDto.setValue(i + j);
					
					connectionLinkDtos.add(connectionLinkDto);
				
				}
			}
		}
		
		return connectionsDto;
	}
	
	
	private void updateMemberInGraph(MemberDto memberDto, List<DeviceRangeDto> devicesInRange){
		
		if(mRoomGraph.containsVertex(memberDto)){
			
			//first remove all the edges from this member
			Set<String> edgeSet = mRoomGraph.edgeSet();
			
			List<String> edgesToRemove = new ArrayList<String>();
			for (String edge : edgeSet) {
				
				if(mRoomGraph.getEdgeSource(edge).equals(memberDto)){
					edgesToRemove.add(edge);
				}
			}
			
			if(edgesToRemove.size() > 0){
				
				mRoomGraph.removeAllEdges(edgesToRemove);
				
			}
		}else{
			
			mRoomGraph.addVertex(memberDto);
		}
		
		//add new edges from the member

		if(devicesInRange.size() > 0){
			
			for (DeviceRangeDto deviceRange : devicesInRange) {
				
				//find the other member
				MemberDto otherMemberDto = null;
				Set<MemberDto> vertexSet = mRoomGraph.vertexSet();
				
				for (MemberDto memberDto2 : vertexSet) {
					
					if(memberDto2.getBluetoothMac().equalsIgnoreCase(deviceRange.getBluetoothMac())){
						otherMemberDto = memberDto2;
						break;
					}
				}
				
				if(otherMemberDto == null){
					
					continue;
					
				}
				String newEdge = memberDto.getBluetoothMac() + "##" + deviceRange.getBluetoothMac();
				mRoomGraph.addEdge(memberDto, otherMemberDto, newEdge);
			}
			
			
		}
	}
	
	public List<Set<MemberDto>> getSubgroups(){
		
		AsUndirectedGraph<MemberDto, String> undirectedGraph = new AsUndirectedGraph<MemberDto, String>(mRoomGraph);
		
		ConnectivityInspector<MemberDto, String> connectivityInspector = new ConnectivityInspector<MemberDto, String>(undirectedGraph);
		
		return connectivityInspector.connectedSets();
		
	}
}
