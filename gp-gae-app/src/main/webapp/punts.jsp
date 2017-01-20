<%-- //[START all]--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- //[START imports]--%>
<%@ page import="java.util.List" %>
<%@ page import="com.goodpunts.GoodPuntsServices" %>
<%@ page import="com.joe.springracing.objects.Meeting" %>
<%@ page import="com.joe.springracing.objects.Race" %>
<%@ page import="com.joe.springracing.objects.Runner" %>
<%@ page import="com.joe.springracing.objects.Punt" %>
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
<% 			List<Meeting> meets = GoodPuntsServices.getSpringRacingDAO().fetchExistingMeets();
			DecimalFormat df = new DecimalFormat("0.0");
			for (Meeting meet : meets) {
				//if (meet.getDate().getTime() > System.currentTimeMills() - 24 * 60 * 60 * 1000) { %>
					<p><%=meet.getDate() %> <%=meet.getVenue() %></p>					
<%					List<Punt> punts = GoodPuntsServices.getPuntingDAO().fetchPuntsForMeet(meet);
					for (Punt punt : punts) {%>
						<p style="margin-left:40px">
						<a href="race_probabilities.jsp?meet=<%=meet.getMeetCode()%>&race_code=<%=punt.getRace().getRaceCode()%>">
						Race <%=punt.getRace().getRaceNumber()%>
						</a>
						&nbsp;
						<%=punt.getType()%>&nbsp;
<% 						for (Runner horse : punt.getRunners()) {%>
							<%= horse.getNumber() + ".&nbsp;" + horse.getHorse() %>&nbsp;
<% 						}%>
						<%=df.format(punt.getJoesOdds())%>&nbsp;
						<%=punt.getBookieOdds()%>
						</p>
<%					}
				//}
			}
%>
</body>
</html>
<%-- //[END all]--%>