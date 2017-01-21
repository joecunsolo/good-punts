<%-- //[START all]--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- //[START imports]--%>
<%@ page import="java.util.List" %>
<%@ page import="com.joe.springracing.business.MeetBusiness" %>
<%@ page import="com.goodpunts.GoodPuntsServices" %>
<%@ page import="com.joe.springracing.objects.Meeting" %>
<%@ page import="com.joe.springracing.objects.Race" %>
<%@ page import="com.joe.springracing.objects.Runner" %>
<%@ page import="com.joe.springracing.objects.Punt" %>
<%@ page import="com.joe.springracing.business.model.AnalysableObjectStatistic" %>
<%@ page import="com.joe.springracing.business.model.stats.SingleVariateStatistic" %>
 <%@ page import="java.text.DecimalFormat" %>
 
<%-- //[END imports]--%>

<%@ page import="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>

<body>
<a href="punts.jsp">Punts</a><br />
<a href="probabilities.jsp">Probabilities</a><br />
<% 			String meetCode = request.getParameter("meet");
			Meeting meet = GoodPuntsServices.getSpringRacingDAO().fetchMeet(meetCode); 
			String raceCode = request.getParameter("race_code");
			Race race = GoodPuntsServices.getSpringRacingDAO().fetchRace(raceCode); %>
						
			<p><%=meet.getDate() %> <%=meet.getVenue() %></p>					
<%			List<Race> races =  GoodPuntsServices.getSpringRacingDAO().fetchRacesForMeet(meet);
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
<% 			}
%>
			</p>		
			<p style="margin-left:40px"><%=race.getRaceNumber()%>&nbsp;<%=race.getName()%>
<% 			List<Runner> runners = GoodPuntsServices.getPuntingDAO().fetchProbabilitiesForRace(race);
			DecimalFormat df = new DecimalFormat("0.0");
			for (Runner runner : runners) {
				List<AnalysableObjectStatistic> stats = runner.getStatistics();
				if (stats != null &&
					stats.size() > 0 &&
					stats.get(0) instanceof SingleVariateStatistic) {
					SingleVariateStatistic svs = (SingleVariateStatistic)stats.get(0);%>
					<p style="margin-left:80px"><%=runner.getNumber()%>&nbsp; 
					<a href="horse.jsp?horse_code=<%=runner.getHorse() %>">
						<%=runner.getHorse()%>
					</a>&nbsp;
					<%=runner.getProbability().getWin()%>&nbsp; 
					<%=df.format(svs.getMean())%>&nbsp;
					<%=df.format(svs.getStandardDeviation())%>&nbsp;
					<%=runner.getOdds().getWin()%></p>
<%				}
			}%>
			</p>
</body>
</html>
<%-- //[END all]--%>