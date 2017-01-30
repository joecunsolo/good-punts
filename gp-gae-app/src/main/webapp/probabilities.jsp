<%-- //[START all]--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- //[START imports]--%>
<%@ page import="java.util.List" %>
<%@ page import="com.joe.springracing.business.MeetBusiness" %>
<%@ page import="com.joe.springracing.business.ProbabilityBusiness" %>
<%@ page import="com.joe.springracing.business.model.Model" %>
<%@ page import="com.joe.springracing.business.model.ModelAttributes" %>
<%@ page import="com.goodpunts.GoodPuntsServices" %>
<%@ page import="com.joe.springracing.objects.Meeting" %>
<%@ page import="com.joe.springracing.objects.Race" %>
<%@ page import="com.joe.springracing.objects.Runner" %>
<%@ page import="com.joe.springracing.objects.Punt" %>
<%@ page import="com.joe.springracing.SpringRacingServices" %>
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
<a href="analysis.jsp">Analysis</a><br />

<% 			MeetBusiness mb = new MeetBusiness();
			Model m = new Model(new ModelAttributes());
			DecimalFormat df = new DecimalFormat("0.0");
			
			ProbabilityBusiness business = new ProbabilityBusiness(
				GoodPuntsServices.getSpringRacingDAO(),
				GoodPuntsServices.getPuntingDAO(),
				SpringRacingServices.getStatistics(), 
				SpringRacingServices.getSimulator(), 
				m);
			List<Meeting> upcoming = business.fetchUpcomingMeets();
			
			mb.sortMeetingsByDate(upcoming);
			for (Meeting meet : upcoming) {
//				if (meet.getDate().getTime() > System.currentTimeMills() - 24 * 60 * 60 * 1000) { %>
					<p><%=meet.getDate() %> <%=meet.getVenue() %></p>					
<%					List<Race> races =  GoodPuntsServices.getSpringRacingDAO().fetchRacesForMeet(meet);
					mb.sortRacesByNumber(races);
					for (Race race : races) {%>
						<p style="margin-left:40px">
							<a href="race_probabilities.jsp?meet=<%=meet.getMeetCode()%>&race_code=<%=race.getRaceCode()%>">
								<%=race.getRaceNumber()%>&nbsp;<%=race.getName()%>
							</a>
 						</p>
<%					}
//				}
			}
%>
</body>
</html>
<%-- //[END all]--%>