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
			<tr>
				<th>Date</th>
				<th>Venue</th>
				<th>Race</th>
				<th>Punt</th>
				<th>Runners</th>
				<th>Odds</th>
				<th>Stake</th>
				<th>Return</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td colspan="8">Open Stakes</td>
			</tr>
<% 			PuntingBusiness business = new PuntingBusiness();
			List<Stake> open = business.fetchOpenStakes();
			DecimalFormat df = new DecimalFormat("0.0");
			for (Stake stake : open) { %>
			<tr>
				<td><%=stake.getDate() %></td>
				<td><%=stake.getVenue() %></td>
				<td>
					<a href="race_probabilities.jsp?race_code=<%=stake.getRaceCode()%>">
					Race <%=stake.getRaceNumber()%>
					</a>
				</td>
				<td><%=stake.getType()%></td>
				<td>
<% 				for (Runner horse : stake.getRunners()) {%>
					<%= horse.getNumber() + ".&nbsp;" + horse.getHorse() %>
<% 				}%>
				</td>
				<td><%=stake.getOdds()%></td>
				<td><%=stake.getAmount()%></td>
				<td><%=stake.getReturn()%></td>
			</tr>
<%			}%>
			<tr>
				<td colspan="8">Settled Stakes</td>
			</tr>
<% 			List<Stake> settled = business.fetchSettledStakes();
			for (Stake stake : settled) { %>
			<tr>
				<td><%=stake.getDate() %></td>
				<td><%=stake.getVenue() %></td>
				<td>
					<a href="race_probabilities.jsp?race_code=<%=stake.getRaceCode()%>">
					Race <%=stake.getRaceNumber()%>
					</a>
				</td>
				<td><%=stake.getType()%></td>
				<td>
<% 				for (Runner horse : stake.getRunners()) {%>
					<%= horse.getNumber() + ".&nbsp;" + horse.getHorse() %>
<% 				}%>
				</td>
				<td><%=stake.getOdds()%></td>
				<td><%=stake.getAmount()%></td>
				<td><%=stake.getReturn()%></td>
			</tr>
<%			}%>
		</tbody>
	</table>
</body>
</html>
<%-- //[END all]--%>