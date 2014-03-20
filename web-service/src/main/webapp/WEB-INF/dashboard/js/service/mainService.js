'use strict';


var mainService = function($log, $http) {
	$log.debug('mainService');

	/*
	this.graph_zoomable_pack_layout = function(data) {
		var w = 400,
		    h = 400,
		    r = 300,
		    x = d3.scale.linear().range([0, r]),
		    y = d3.scale.linear().range([0, r]),
		    node,
		    root;

		var pack = d3.layout.pack()
		    .size([r, r])
		    .value(function(d) { return d.size; })

		var vis = d3.select("#graph_zoomable_pack_layout").insert("svg:svg", "h2")
		    .attr("width", w)
		    .attr("height", h)
		  .append("svg:g")
		    .attr("transform", "translate(" + (w - r) / 2 + "," + (h - r) / 2 + ")");

		//d3.json("graph_zoomable_pack_layout.json", function(data) {
		  node = root = data;

		  var nodes = pack.nodes(root);

		  vis.selectAll("circle")
		      .data(nodes)
		    .enter().append("svg:circle")
		      .attr("class", function(d) { return d.children ? "parent" : "child"; })
		      .attr("cx", function(d) { return d.x; })
		      .attr("cy", function(d) { return d.y; })
		      .attr("r", function(d) { return d.r; })
		      .on("click", function(d) { return zoom(node == d ? root : d); });

		  vis.selectAll("#graph_zoomable_pack_layout text")
		      .data(nodes)
		    .enter().append("svg:text")
		      .attr("class", function(d) { return d.children ? "parent" : "child"; })
		      .attr("x", function(d) { return d.x; })
		      .attr("y", function(d) { return d.y; })
		      .attr("dy", ".35em")
		      .attr("text-anchor", "middle")
		      .style("opacity", function(d) { return d.r > 20 ? 1 : 0; })
		      .text(function(d) { return d.name; });

		  d3.select(window).on("click", function() { zoom(root); });
		//});

		function zoom(d, i) {
		  var k = r / d.r / 2;
		  x.domain([d.x - d.r, d.x + d.r]);
		  y.domain([d.y - d.r, d.y + d.r]);

		  var t = vis.transition()
		      .duration(d3.event.altKey ? 7500 : 750);

		  t.selectAll("circle")
		      .attr("cx", function(d) { return x(d.x); })
		      .attr("cy", function(d) { return y(d.y); })
		      .attr("r", function(d) { return k * d.r; });

		  t.selectAll("text")
		      .attr("x", function(d) { return x(d.x); })
		      .attr("y", function(d) { return y(d.y); })
		      .style("opacity", function(d) { return k * d.r > 20 ? 1 : 0; });

		  node = d;
		  d3.event.stopPropagation();
		}

	};
	
	
	this.graph_hierarchical_edge_bundling = function() {

		var w = 500,
		    h = 500,
		    rx = w / 2,
		    ry = h / 2,
		    m0,
		    rotate = 0;

		var splines = [];

		var cluster = d3.layout.cluster()
		    .size([360, ry - 120])
		    .sort(function(a, b) { return d3.ascending(a.key, b.key); });

		var bundle = d3.layout.bundle();

		var line = d3.svg.line.radial()
		    .interpolate("bundle")
		    .tension(.85)
		    .radius(function(d) { return d.y; })
		    .angle(function(d) { return d.x / 180 * Math.PI; });

		// Chrome 15 bug: <http://code.google.com/p/chromium/issues/detail?id=98951>
		var div = d3.select("#graph_hierarchical_edge_bundling").insert("div", "h2")
		    .style("width", w + "px")
		    .style("height", w + "px")
		    .style("position", "absolute")
		    .style("-webkit-backface-visibility", "hidden");

		var svg = div.append("svg:svg")
		    .attr("width", w)
		    .attr("height", w)
		  .append("svg:g")
		    .attr("transform", "translate(" + rx + "," + ry + ")");

		svg.append("svg:path")
		    .attr("class", "arc")
		    .attr("d", d3.svg.arc().outerRadius(ry - 120).innerRadius(0).startAngle(0).endAngle(2 * Math.PI))
		    .on("mousedown", mousedown);

		d3.json("graph_hierarchical_edge_bundling.json", function(classes) {
		  var nodes = cluster.nodes(packages.root(classes)),
		      links = packages.imports(nodes),
		      splines = bundle(links);

		  var path = svg.selectAll("path.link")
		      .data(links)
		    .enter().append("svg:path")
		      .attr("class", function(d) { return "link source-" + d.source.key + " target-" + d.target.key; })
		      .attr("d", function(d, i) { return line(splines[i]); });

		  svg.selectAll("g.node")
		      .data(nodes.filter(function(n) { return !n.children; }))
		    .enter().append("svg:g")
		      .attr("class", "node")
		      .attr("id", function(d) { return "node-" + d.key; })
		      .attr("transform", function(d) { return "rotate(" + (d.x - 90) + ")translate(" + d.y + ")"; })
		    .append("svg:text")
		      .attr("dx", function(d) { return d.x < 180 ? 8 : -8; })
		      .attr("dy", ".31em")
		      .attr("text-anchor", function(d) { return d.x < 180 ? "start" : "end"; })
		      .attr("transform", function(d) { return d.x < 180 ? null : "rotate(180)"; })
		      .text(function(d) { return d.key; })
		      .on("mouseover", mouseover)
		      .on("mouseout", mouseout);

		  d3.select("input[type=range]").on("change", function() {
		    line.tension(this.value / 100);
		    path.attr("d", function(d, i) { return line(splines[i]); });
		  });
		});

		d3.select(window)
		    .on("mousemove", mousemove)
		    .on("mouseup", mouseup);

		function mouse(e) {
		  return [e.pageX - rx, e.pageY - ry];
		}

		function mousedown() {
		  m0 = mouse(d3.event);
		  d3.event.preventDefault();
		}

		function mousemove() {
		  if (m0) {
		    var m1 = mouse(d3.event),
		        dm = Math.atan2(cross(m0, m1), dot(m0, m1)) * 180 / Math.PI;
		    div.style("-webkit-transform", "translateY(" + (ry - rx) + "px)rotateZ(" + dm + "deg)translateY(" + (rx - ry) + "px)");
		  }
		}

		function mouseup() {
		  if (m0) {
		    var m1 = mouse(d3.event),
		        dm = Math.atan2(cross(m0, m1), dot(m0, m1)) * 180 / Math.PI;

		    rotate += dm;
		    if (rotate > 360) rotate -= 360;
		    else if (rotate < 0) rotate += 360;
		    m0 = null;

		    div.style("-webkit-transform", null);

		    svg
		        .attr("transform", "translate(" + rx + "," + ry + ")rotate(" + rotate + ")")
		      .selectAll("g.node text")
		        .attr("dx", function(d) { return (d.x + rotate) % 360 < 180 ? 8 : -8; })
		        .attr("text-anchor", function(d) { return (d.x + rotate) % 360 < 180 ? "start" : "end"; })
		        .attr("transform", function(d) { return (d.x + rotate) % 360 < 180 ? null : "rotate(180)"; });
		  }
		}

		function mouseover(d) {
		  svg.selectAll("path.link.target-" + d.key)
		      .classed("target", true)
		      .each(updateNodes("source", true));

		  svg.selectAll("path.link.source-" + d.key)
		      .classed("source", true)
		      .each(updateNodes("target", true));
		}

		function mouseout(d) {
		  svg.selectAll("path.link.source-" + d.key)
		      .classed("source", false)
		      .each(updateNodes("target", false));

		  svg.selectAll("path.link.target-" + d.key)
		      .classed("target", false)
		      .each(updateNodes("source", false));
		}

		function updateNodes(name, value) {
		  return function(d) {
		    if (value) this.parentNode.appendChild(this);
		    svg.select("#node-" + d[name].key).classed(name, value);
		  };
		}

		function cross(a, b) {
		  return a[0] * b[1] - a[1] * b[0];
		}

		function dot(a, b) {
		  return a[0] * b[0] + a[1] * b[1];
		}
	};
	*/
	this.graph_Better_force_layout_selection = function(data){
	
		function name(d) { return d.name; }
		function group(d) { return d.group; }
		
		var color = d3.scale.category10();
		function colorByGroup(d) { return color(group(d)); }
		
		var width = 800,
		    height = 800;
		
		var svg = d3.select('#graph_Better_force_layout_selection')
		    .append('svg')
		    .attr('width', width)
		    .attr('height', height);
		
		var node, link;
		
		var voronoi = d3.geom.voronoi()
		    .x(function(d) { return d.x; })
		    .y(function(d) { return d.y; })
		    .clipExtent([[-10, -10], [width+10, height+10]]);
		
		function recenterVoronoi(nodes) {
		    var shapes = [];
		    voronoi(nodes).forEach(function(d) {
		        if ( !d.length ) return;
		        var n = [];
		        d.forEach(function(c){
		            n.push([ c[0] - d.point.x, c[1] - d.point.y ]);
		        });
		        n.point = d.point;
		        shapes.push(n);
		    });
		    return shapes;
		}
		
		var force = d3.layout.force()
		    .charge(-2000)
		    .friction(0.3)
		    .linkDistance(50)
		    .size([width, height]);
		
		force.on('tick', function() {
		    node.attr('transform', function(d) { return 'translate('+d.x+','+d.y+')'; })
		        .attr('clip-path', function(d) { return 'url(#clip-'+d.index+')'; });
		
		    link.attr('x1', function(d) { return d.source.x; })
		        .attr('y1', function(d) { return d.source.y; })
		        .attr('x2', function(d) { return d.target.x; })
		        .attr('y2', function(d) { return d.target.y; });
		
		    var clip = svg.selectAll('.clip')
		        .data( recenterVoronoi(node.data()), function(d) { return d.point.index; } );
		
		    clip.enter().append('clipPath')
		        .attr('id', function(d) { return 'clip-'+d.point.index; })
		        .attr('class', 'clip');
		    clip.exit().remove()
		
		    clip.selectAll('path').remove();
		    clip.append('path')
		        .attr('d', function(d) { return 'M'+d.join(',')+'Z'; });
		});
		
		//d3.json('graph_better_force_layout_selection.json', function(err, data) {
		
		    data.nodes.forEach(function(d, i) {
		        d.id = i;
		    });
		
		    link = svg.selectAll('.link')
		        .data( data.links )
		      .enter().append('line')
		        .attr('class', 'link')
		        .style("stroke-width", function(d) { return Math.sqrt(d.value); });
		
		    node = svg.selectAll('.node')
		        .data( data.nodes )
		      .enter().append('g')
		        .attr('title', name)
		        .attr('class', 'node')
		        .call( force.drag );
		
		    node.append('circle')
		        .attr('r', 30)
		        .attr('fill', colorByGroup)
		        .attr('fill-opacity', 0.5);
		    
		    node.append('text')	
		      .text(function(d) { return d.name; });
		    
		    
		    node.append('circle')
		        .attr('r', 4)
		        .attr('stroke', 'black');
		
		    force
		        .nodes( data.nodes )
		        .links( data.links )
		        .start();
		//});
	
	};
	
	this.members = function(data){
		if(data.members.length > 0){
			var members = '<ul>';
			for(var i=0;i<data.members.length;i++){
		        var obj = data.members[i];
		        var name = obj.firstName + " " + obj.lastName;
		        var imageUrl = obj.imageUrl;
		        console.log("name: " + name);
		        console.log("imageUrl: " + imageUrl);
		        if(obj.panic == "PANIC!!!"){
		        	members +='<li class="pepole background_red">';
		        }else{
		        	members +='<li class="pepole">';
		        }
		        members +='<img src="' + imageUrl + '" width="40px" height="40px"/>' + name + '</li>';
		    }
			members +='</ul>';
			console.log(members);
			$("#list_of_pepole").html(members);
		}
	};
};