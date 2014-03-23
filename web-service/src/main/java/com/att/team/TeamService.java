package com.att.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import com.att.team.dtos.LastSeenByEntry;
import com.att.team.dtos.MemberDto;
import com.att.team.dtos.MembersDto;
import com.att.team.dtos.RequestDto;
import com.att.team.dtos.ResponseDto;

@Service
public class TeamService {

	private static final long MAX_TIME_ALONE_BEFORE_PANIC_MILLIS = 20000;
	
	private static final String BASE_IMAGE_STORE_PATH = "http://ronen.doronsolomon.com/teamkeeper/";
	
	SimpleDirectedGraph<MemberDto, String> mRoomGraph = new SimpleDirectedGraph<MemberDto, String>(String.class);
	
	Map<String, Long> mLonelyMembersDurationsMap = new HashMap<String, Long>();
	
	public synchronized ResponseDto memberDataReceived(RequestDto requestDto){
		
		MemberDto memberDto = requestDto.getMemberDto();		
		memberDto.setLastUpdateTime(System.currentTimeMillis());
		
		List<DeviceRangeDto> devicesInRange = requestDto.getDevicesInRange();
		
		updateMemberInGraph(memberDto, devicesInRange);
		
		ResponseDto responseDto = new ResponseDto();
		
		responseDto.setMembers(new ArrayList<MemberDto>(mRoomGraph.vertexSet()));
		List<Set<MemberDto>> subgroups = getSubgroups();
		responseDto.setGroups(subgroups);
		
		updateLastSeenByMap(subgroups);
		
		return responseDto;
		
	}
	
	
	public synchronized CirclesDto getCirclesDto(){
		
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
	
	
	
	public synchronized ConnectionsDto getConnectionsDto(String mac){
		
		String[] indexes = new String[mRoomGraph.vertexSet().size()];
		
		
		
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
				
				if(memberDto.getBluetoothMac().equalsIgnoreCase(mac)){
					
					curMemberDto.setGroup(200);
				
				}else{
					
					curMemberDto.setGroup(groupIndex);
				}
				
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
	
	public synchronized MembersDto getMembers(){
		
		MembersDto membersDto = new MembersDto();
		membersDto.setMembers(new ArrayList<MemberDto>(mRoomGraph.vertexSet()));
		
		return membersDto;
	}
	
	
	private synchronized void updateMemberInGraph(MemberDto memberDto, List<DeviceRangeDto> devicesInRange){
		
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
			
			memberDto.setImageUrl(BASE_IMAGE_STORE_PATH + memberDto.getBluetoothMac().replaceAll(":", "_") + ".png");
			mRoomGraph.addVertex(memberDto);
		}
		
		Iterator<MemberDto> iterator = mRoomGraph.vertexSet().iterator();
		
		while (iterator.hasNext()) {
			
			MemberDto dto = iterator.next();
			
			if(dto.equals(memberDto)){
				
				dto.setLastUpdateTime(System.currentTimeMillis());
				break;
			}
			
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
	
	public synchronized List<Set<MemberDto>> getSubgroups(){
		
		AsUndirectedGraph<MemberDto, String> undirectedGraph = new AsUndirectedGraph<MemberDto, String>(mRoomGraph);
		
		ConnectivityInspector<MemberDto, String> connectivityInspector = new ConnectivityInspector<MemberDto, String>(undirectedGraph);
		
		return connectivityInspector.connectedSets();
		
	}
	
	
	private synchronized void updateLastSeenByMap(List<Set<MemberDto>> groups){
		
		for (Set<MemberDto> group : groups) {
			
			if(group.size() > 1){
				
				for (MemberDto memberDto : group) {
					
					List<LastSeenByEntry> lastSeenByEntries = new ArrayList<LastSeenByEntry>();
					Set<MemberDto> groupCopy = new HashSet<MemberDto>(group);
					groupCopy.remove(memberDto);
					
					for (MemberDto groupCopyMember : groupCopy) {
						
						LastSeenByEntry lastSeenByEntry = new LastSeenByEntry();
						lastSeenByEntry.setName(groupCopyMember.getFirstName());
						lastSeenByEntry.setLastName(groupCopyMember.getLastName());
						lastSeenByEntry.setPhone(groupCopyMember.getMobileNumber());
						lastSeenByEntry.setPhotoUrl(groupCopyMember.getImageUrl());
						
						lastSeenByEntries.add(lastSeenByEntry);
					}
					
					memberDto.setLastSeenBy(lastSeenByEntries);
					memberDto.setPanic(null);
					
					mLonelyMembersDurationsMap.remove(memberDto.getBluetoothMac());
				}
			
			}else{
				
				MemberDto memberDto = group.iterator().next();
				
				Long lonelySinceLong = mLonelyMembersDurationsMap.get(memberDto.getBluetoothMac());
				
				long lonelySince = lonelySinceLong != null ? lonelySinceLong.longValue() : 0;
				
				long now = System.currentTimeMillis();
				
				if(lonelySince > 0){
					
					if((now - lonelySince) > MAX_TIME_ALONE_BEFORE_PANIC_MILLIS){
						
						memberDto.setPanic("PANIC!!!");
					
					}else{
						
						memberDto.setPanic(null);
					}
				}else{
					
					mLonelyMembersDurationsMap.put(memberDto.getBluetoothMac(), now);
					memberDto.setPanic(null);
				}
			}
			
		}
	}
}
