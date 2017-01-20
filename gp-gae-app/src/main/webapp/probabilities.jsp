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
<% 			MeetBusiness mb = new MeetBusiness();
			List<Meeting> meets = GoodPuntsServices.getSpringRacingDAO().fetchExistingMeets();
			mb.sortMeetingsByDate(meets);
			for (Meeting meet : meets) {
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