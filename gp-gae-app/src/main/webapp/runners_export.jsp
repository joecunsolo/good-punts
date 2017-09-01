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
    	
    	function loadRaces() {
    		$.ajax({ url: 'json/json_races.jsp', 
				//dataType: 'html',
	        	success: function(data) { 
	        		var races = data.races;
	        		for (i = 0; i < races.length; i++) {
	        			loadRace(races[i]);
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
   			    alert( "Sorry, there was a problem!" );
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
		Date,Distance,Runners,Venue,PrizeMoney,Jockey,Trainer,Number,Horse,Mean,SD,Weight,Win%,WinOdds,Scratched,Emergency,Result
		</div>
	</body>
</html>
<%-- //[END all]--%>