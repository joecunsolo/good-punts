<%-- //[START all]--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- //[START imports]--%>
<%-- //[END imports]--%>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
    <script src="javascript/jquery-3.2.1.min.js"></script>
    
    <script>
    	//set the from and to dates
		var from;
    	var to;
    	
    	$( document ).ready(function() {
	        console.log( "ready!" );
	        setDates();
	    });
    	
    	function setDates() {
	    	var today = new Date();
		    var to_dd = today.getDate()+1; //tomorrow - midnight tonight
		    var to_mm = today.getMonth()+1; //January is 0!
		    var to_yy = today.getFullYear();
		    
		    var from_dd = to_dd;
		    var from_mm = to_mm - 1; // last month
		    var from_yy = to_yy;
		    
		    if(from_mm == 0) { // it's january...
		    	from_mm = 12;  // make it from december
		    	from_yy = from_yy - 1; //last year
		    }
		    if(from_mm < 10) {
		    	from_mm = '0'+from_mm;
		    }
		    if(to_mm < 10) {
		    	to_mm = '0'+to_mm;
		    }
		    
		    if(from_dd<10) {
		    	from_dd = '0'+from_dd;
		    } 
		    if(to_dd<10) {
		    	to_dd = '0'+to_dd;
		    } 
		    
		    document.dates.from.value = from_yy + '-' + from_mm + '-' + from_dd;
		    document.dates.to.value = to_yy + '-' + to_mm + '-' + to_dd;
    	}
	    
    </script>
</head>

<body>
	<jsp:include page="menu.jsp" />
	<p>
		<form name="dates">
			from: <input name="from" type="text" value=""> <br />
			to: <input name="to" type="text" value=""><br />
			results: <input name="results" type="text" value="true">
			splits: <input name="splits" type="text" value="true">
		</form>	
	</p>
	<p>
		<a onclick="window.location='/history_export.jsp?splits='+ document.dates.splits.value +'&from='+ document.dates.from.value +'&to='+ document.dates.to.value" href="#">Horse Histories</a><br />					
		<a onclick="window.location='/runners_export.jsp?results='+ document.dates.results.value +'&from='+ document.dates.from.value +'&to='+ document.dates.to.value" href="#">Runner Results</a>
	</p>					
	
</body>
</html>
<%-- //[END all]--%>