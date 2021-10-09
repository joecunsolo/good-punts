<%-- //[START all]--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
    <script src="javascript/jquery-3.2.1.min.js"></script>
    <script>
	    $( document ).ready(function() {
	        console.log( "ready!" );
	        loadRaces();
	    });
	    
	    function parse_query_string(query) {
    	  var vars = query.split("&");
    	  var query_string = {};
    	  for (var i = 0; i < vars.length; i++) {
    	    var pair = vars[i].split("=");
    	    var key = decodeURIComponent(pair[0]);
    	    var value = decodeURIComponent(pair[1]);
    	    // If first entry with this name
    	    if (typeof query_string[key] === "undefined") {
    	      query_string[key] = decodeURIComponent(value);
    	      // If second entry with this name
    	    } else if (typeof query_string[key] === "string") {
    	      var arr = [query_string[key], decodeURIComponent(value)];
    	      query_string[key] = arr;
    	      // If third or later entry with this name
    	    } else {
    	      query_string[key].push(decodeURIComponent(value));
    	    }
    	  }
    	  return query_string;
    	}
    	
	    function loadRaces() {
    		var query = window.location.search.substring(1);
    		var qs = parse_query_string(query);
    		var url = 'api/races/?results=true';
    		//add the parameters if they exist
    		if (qs.splits != "undefeined") {
    			url += '&splits=' + qs.splits;
    		}
    		if (qs.from != "undefeined") {
    			url += '&from=' + qs.from;
    		}
    		if (qs.to != "undefeined") {
    			url += '&to=' + qs.to;
    		}
    		console.log(url);
    		$.ajax({ url: url, 
				//dataType: 'html',
	        	success: function(races) { 
	        		for (i = 0; i < races.length; i++) {
	        			console.log( races[i].raceCode );
	        			loadHorses(races[i].runners);
	        		}
	             } })
	    	.fail(function( xhr, status, errorThrown ) {
			    alert( "Sorry, there was a problem!" );
			    console.log( "Error: " + errorThrown );
			    console.log( "Status: " + status );
			    console.dir( xhr );
			  });
    	}
	    
    	function loadHorses(runners) {
    		for (j = 0; j < runners.length; j++) {
    			loadHorse(runners[j].horse);
    		}
    	}
    	
    	function loadHorse(id) {
    		console.log( id );
   			$.ajax({ url: 'json/json_horse.jsp?id=' + id, 
   				dataType: 'html',
   	        	success: function(data) { 
   	        		$( "#csv-content" ).append(data);
   	             } })
   	    	.fail(function( xhr, status, errorThrown ) {
   			    alert( "Sorry, there was a problem! " + racecode);
   			    console.log( "Error: " + errorThrown );
   			    console.log( "Status: " + status );
   			    console.dir( xhr );
   			  });
    	}
	</script>
</head>

	<body>
		<jsp:include page="menu.jsp" />
		<div id="csv-content">
		Horse,AvgPrizeMoney,TotPrizeMoney,Colour,Age,Sex,Date,DaysAgo,Venue,Barrier,Weight,Rating,Jockey,Trainer,Distance,TrackCondition,GoodAtClass,GoodAtDistance,GoodAtTrack,GoodAtTrackCondition,Trial,RacePrizeMoney,Position,PrizeMoney,RaceTime,Scratched,OddStart,200m,400m,600m,800m,1000m,1200m,1400m,1600m,1800m,2000m,2200m,2400m,2600m,2800m,3000m,3200m<br/>
		</div>
	</body>
</html>
<%-- //[END all]--%>