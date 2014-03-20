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

import com.att.team.dtos.DeviceRange;
import com.att.team.dtos.MemberDto;
import com.att.team.dtos.RequestDto;
import com.att.team.dtos.ResponseDto;

@Service
public class TeamService {

	SimpleDirectedGraph<MemberDto, String> mRoomGraph;
	
	Map<String, MemberDto> mMembers = new HashMap<String, MemberDto>();
	
	public ResponseDto memberDataReceived(RequestDto requestDto){
		
		MemberDto memberDto = requestDto.getMemberDto();		
		memberDto.setLastUpdatetime(System.currentTimeMillis());
		
		if(mMembers.containsKey(memberDto.getBluetoothMac())){
			
			mMembers.get(memberDto.getBluetoothMac()).setLastUpdatetime(System.currentTimeMillis());
		
		}else{
			
			
			mMembers.put(memberDto.getBluetoothMac(), memberDto);
		}

		List<DeviceRange> devicesInRange = requestDto.getDevicesInRange();
		
		updateMemberInGraph(memberDto, devicesInRange);
		
		ResponseDto responseDto = new ResponseDto();
		
		responseDto.setMembers(new ArrayList<MemberDto>(mMembers.values()));
		responseDto.setGroups(getSubgroups());
		
		return responseDto;
		
	}
	
	
	private void updateMemberInGraph(MemberDto memberDto, List<DeviceRange> devicesInRange){
		
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
			
			for (DeviceRange deviceRange : devicesInRange) {
				
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
