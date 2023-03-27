<%-- //[START all]--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- //[START imports]--%> 
<%@ page import="com.joe.springracing.SpringRacingServices" %>
<%@ page import="java.text.DecimalFormat" %>
<%-- //[END imports]--%>
<%com.example.guestbook.OfyHelper.init();%>
<div>
	<div style="float:right">
	    <%DecimalFormat df = new DecimalFormat("#,###.00"); %>
		$<%=df.format(SpringRacingServices.getBookieAccount().fetchAccountAmount())%>
		<a href="history_export.jsp">Export</a><br />
	</div>
	<div>
		<a href="picks.jsp">Picks</a><br />
		<a href="punts.jsp">Punts</a><br />
		<a href="probabilities.jsp">Probabilities</a><br />
		<a href="peformance.jsp">Performance</a><br />
		<a href="analysis.jsp">Analysis</a><br />
	</div>
</div>
<%-- //[END all]--%>