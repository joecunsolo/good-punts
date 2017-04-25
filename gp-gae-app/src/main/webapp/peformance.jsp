<%-- //[START all]--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- //[START imports]--%>
<%@ page import="java.util.List" %>
<%@ page import="com.joe.springracing.business.PuntingBusiness" %>
<%@ page import="com.joe.springracing.objects.Race" %>
<%@ page import="com.joe.springracing.objects.Runner" %>
<%@ page import="com.joe.springracing.objects.Punt" %>
<%@ page import="com.joe.springracing.objects.Stake" %>
<%@ page import="com.joe.springracing.SpringRacingServices" %>
<%@ page import="java.text.DecimalFormat" %>

<%-- //[END imports]--%>

<%@ page import="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
    <style>
	    table {
	    	border-collapse: collapse;
	    }
		th, td {
		    border: 1px solid #ddd;
    		text-align: left;
    		padding: 15px;
		}
	</style>
</head>

<body>
	<jsp:include page="menu.jsp" />
	<table>
		<thead>
			<th>Punt</th>
			<th>Runner</th>
			<th>Odds</th>
			<th>Stake</th>
			<th>Return</th>
		</thead>
		<tbody>
<% 			PuntingBusiness business = new PuntingBusiness();
			List<Punt> settled = business.fetchSettledPunts();
			DecimalFormat df = new DecimalFormat("0.0");
			for (Punt punt : settled) { 
				for (Stake stake : punt.getStakes()) { %>
				<tr>
					<td><%=punt.getRace().getDate() %></td>
					<td><%=punt.getRace().getVenue() %></td>
					<td>
						<a href="race_probabilities.jsp?race_code=<%=punt.getRace().getRaceCode()%>">
						Race <%=punt.getRace().getRaceNumber()%>
						</a>
					</td>
					<td><%=punt.getType()%></td>
					<td>
<% 					for (Runner horse : punt.getRunners()) {%>
						<%= horse.getNumber() + ".&nbsp;" + horse.getHorse() %>
<% 					}%>
					</td>
					<td><%=stake.getOdds()%></td>
					<td><%=stake.getAmount()%></td>
					<td><%=stake.getReturn()%></td>
				</tr>
<%				}
			}%>
		</tbody>
	</table>
</body>
</html>
<%-- //[END all]--%>