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
    		var url = 'api/races/?';
    		//add the parameters if they exist
    		if (typeof qs.splits !== 'undefined') {
    			url += 'splits='+ qs.splits;
    		}
    		if (typeof qs.results !== 'undefined') {
    			url += '&results='+ qs.results;
    		}
    		if (typeof qs.from !== 'undefined') {
    			url += '&from=' + qs.from;
    		}
    		if (typeof qs.to !== 'undefined') {
    			url += '&to=' + qs.to;
    		}
    		console.log(url);
    		$.ajax({ url: url, 
				//dataType: 'html',
	        	success: function(races) { 
	        		for (i = 0; i < races.length; i++) {
	        			loadRace(races[i].raceCode);
	        		}
	             } })
	    	.fail(function( xhr, status, errorThrown ) {
			    alert( "Sorry, there was a problem!" );
			    console.log( "Error: " + errorThrown );
			    console.log( "Status: " + status );
			    console.dir( xhr );
			  });
    	}
    	
    	function loadRace(raceCode) {
    		console.log( raceCode );
   			$.ajax({ url: 'json/json_probabilities.jsp?race_code=' + raceCode, 
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
		<div id="csv-content">
		Horse,AvgPrizeMoney,TotPrizeMoney,Colour,Age,Sex,Date,Venue,Barrier,Weight,Rating,Jockey,Trainer,Distance,TrackCondition,Trial,RacePrizeMoney,Position,PrizeMoney,RaceTime,Scratched,OddStart<br/>
		</div>
	</body>
</html>
<%-- //[END all]--%>