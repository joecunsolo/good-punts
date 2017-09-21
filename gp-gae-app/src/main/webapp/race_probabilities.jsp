<%-- //[START all]--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- //[START imports]--%>
<%@ page import="java.util.List" %>
<%@ page import="com.joe.springracing.business.MeetBusiness" %>
<%@ page import="com.joe.springracing.SpringRacingServices" %>
<%@ page import="com.joe.springracing.objects.Meeting" %>
<%@ page import="com.joe.springracing.objects.Race" %>
<%@ page import="com.joe.springracing.objects.Runner" %>
<%@ page import="com.joe.springracing.objects.Punt" %>
<%@ page import="com.joe.springracing.objects.Horse" %>
<%@ page import="com.joe.springracing.business.model.AnalysableObjectStatistic" %>
<%@ page import="com.joe.springracing.business.model.stats.SingleVariateStatistic" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.Comparator" %>
 
<%-- //[END imports]--%>

<%@ page import="java.util.List" %>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>

<body>
	<jsp:include page="menu.jsp" />

<% 			String raceCode = request.getParameter("race_code");
			Race race = SpringRacingServices.getSpringRacingDAO().fetchRace(raceCode);
			String meetCode = race.getMeetCode();
			Meeting meet = SpringRacingServices.getSpringRacingDAO().fetchMeet(meetCode); %>
						
			<p><%=meet.getDate() %> <%=meet.getVenue() %></p>					
<%			List<Race> races =  SpringRacingServices.getSpringRacingDAO().fetchRacesForMeet(meet);
			MeetBusiness mb = new MeetBusiness();
			mb.sortRacesByNumber(races); %>
			
			<p style="margin-left:40px">
<% 			for (Race n : races) {
				if (race.getRaceNumber() != n.getRaceNumber()) {%>			
					<a href="?meet=<%=meetCode%>&race_code=<%=n.getRaceCode()%>"><%=n.getRaceNumber()%></a>
				<%} else {%>
					<%=n.getRaceNumber()%>
				<%} %>
				&nbsp;&nbsp;&nbsp;
<% 			}%>
			</p>		
			<p style="margin-left:40px">
			<%=race.getRaceNumber()%>&nbsp;<%=race.getName()%>
			<table>
				<thead>
					<tr>
						<td>#</td>
						<td>Name</td>
						<td>Mean</td>
						<td>SD</td>
						<td>Weight</td>
						<td>%</td>
						<td>Odds</td>
						<td>Spell</td>
						<td># Races</td>
						<td></td>
					</tr>
				</thead>
				<tbody>
<% 			List<Runner> runners = SpringRacingServices.getPuntingDAO().fetchProbabilitiesForRace(race);
			int[] result = race.getResult();
			DecimalFormat df = new DecimalFormat("0.0");
			for (Runner runner : runners) {
				Horse h = SpringRacingServices.getSpringRacingDAO().fetchHorse(runner.getHorse());
				%>
					<tr>
						<td><%=runner.getNumber()%></td> 
						<td><a href="horse.jsp?horse_code=<%=runner.getHorse() %>">
							<%=runner.getHorse()%>
						</a></td>
						<td><%=df.format(runner.getProbability().getMean())%></td>
						<td><%=df.format(runner.getProbability().getStandardDeviation())%></td>
						<td><%=df.format(runner.getWeight())%></td>
						<td><%=df.format(runner.getProbability().getWin() * 100)%></td> 
						<td><%=runner.getOdds().getWin()%></td>
						<td><%=h.getSpell()%></td>
						<td><%=h.getNumberOfRaces()%></td>
						<td>
							<%=runner.isScratched() ? "SCR" : ""%>
							<%=runner.isEmergency() ? "EMG" : ""%>
							<%if (result != null) {
								for (int i = 0; i < result.length; i++) {
									if (runner.getNumber() == result[i]) {%>
										<%=i+1 %>
<%									}
								}
							} %>
						</td>
					</tr>
<%				//}
			}%>
				</tbody>
			</table>
</body>
</html>
<%-- //[END all]--%>