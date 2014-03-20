'use strict';

var mainViewModel = function($scope, $log, $state, $stateParams, mainService) {
	$log.debug('mainViewModel');
	
	//mainService.graph_zoomable_pack_layout();
	//mainService.graph_hierarchical_edge_bundling();
	//mainService.graph_Better_force_layout_selection();
	/*
	$.ajax({
		type : "GET",
		url : "http://localhost:8089/team/v1/webapp/circles",
		contentType: "application/json",
		async : true,
		success : function(data, textStatus, request) {
			console.log(data);
			mainService.graph_zoomable_pack_layout(data);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("login error: " + XMLHttpRequest.status + " Error: "
					+ errorThrown);
		}
	});	
	*/
	$.ajax({
		type : "GET",
		url : "http://localhost:8089/team/v1/webapp/connections",
		contentType: "application/json",
		async : true,
		success : function(data, textStatus, request) {
			console.log(data);
			mainService.graph_Better_force_layout_selection(data);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("login error: " + XMLHttpRequest.status + " Error: "
					+ errorThrown);
		}
	});	
	
	$.ajax({
		type : "GET",
		url : "http://localhost:8089/team/v1/webapp/members",
		contentType: "application/json",
		async : true,
		success : function(data, textStatus, request) {
			console.log(data);
			//mainService.graph_Better_force_layout_selection(data);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert("login error: " + XMLHttpRequest.status + " Error: "
					+ errorThrown);
		}
	});	
};