<%-- //[START all]--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- //[START imports]--%>
<%@ page import="java.util.List" %>
<%@ page import="com.joe.springracing.SpringRacingServices" %>
<%@ page import="com.joe.springracing.objects.RunnerResult" %>
<%@ page import="com.joe.springracing.objects.Horse" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Comparator" %>
 
<%-- //[END imports]--%>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>

<body>
	<jsp:include page="menu.jsp" />

<% 			String horseCode = request.getParameter("horse_code");
			Horse horse = SpringRacingServices.getSpringRacingDAO().fetchHorse(horseCode); %>
			<p><%=horse.getName() %></p>
			<table>
				<thead>
					<tr>
						<td>Date</td>
						<td>Venue</td>
						<td>Barrier</td>
						<td>Weight</td>
						<td>Rating</td>
						<td>Jockey</td>
						<td>Trainer</td>
						<td>Distance</td>
						<td>Track Condition</td>
						<td>Trial</td>
						<td>Race Prize Money</td>
						<td>Position</td>
						<td>Prize Money</td>
						<td>Race Time</td>
						<td>Scratched</td>
						<td>Splits</td>
					</tr>
				</thead>	
				<tbody>				
<%			List<RunnerResult> results =  horse.getPastResults();
			Collections.sort(results, new Comparator<RunnerResult>() {
				public int compare(RunnerResult o1, RunnerResult o2) {
					if (o1.getRaceDate() == null && o2.getRaceDate() == null) {
						return 0;
					}
					if (o1.getRaceDate() == null) {
						return 1;
					}
					if (o2.getRaceDate() == null) {
						return -1;
					}
					return (int)(o2.getRaceDate().getTime() - o1.getRaceDate().getTime());
				}});
			
 			for (RunnerResult r : results) {%>
					<tr>
					<td><%=r.getRaceDate() %></td>
					<td><%=r.getVenueName()%></td>
					<td><%=r.getBarrier() %></td>
					<td><%=r.getWeight() %></td>
					<td><%=r.getRating() %></td>
					<td><%=r.getJockey()%></td>
					<td><%=r.getTrainer()%></td>
					<td><%=r.getDistance()%></td>
					<td><%=r.getTrackCondition()%></td>
					<td><%=r.isTrial() %></td>
					<td><%=r.getRacePrizeMoney() %></td>
					<td><%=r.getPosition() %></td>
					<td><%=r.getPrizeMoney() %></td>
					<td><%=r.getRaceTime() %></td>
					<td><%=r.isScratched() %></td>
					<td>		
<%					if (r.getSplits() != null) {
						for (Double split : r.getSplits()) { %>
							<%=split %>&nbsp;
<%						}
					}
					%>
					</td>
					</tr>
<% 			}%>
				</tbody>
			</table>
<!-- @TODO Need to be able to get upcoming races for a horse -->

</body>
</html>
<%-- //[END all]--%>