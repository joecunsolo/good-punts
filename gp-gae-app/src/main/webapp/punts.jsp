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
<%@ page import="java.text.DecimalFormat" %>

<%-- //[END imports]--%>

<%@ page import="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
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
			<th></th>
			<th>Race</th>
			<th>Punt</th>
			<th>Runner</th>
			<th>Odds</th>
			<th>Market</th>
		</thead>
		<tbody>
<% 			MeetBusiness mb = new MeetBusiness();
			Model m = new Model(new ModelAttributes());

			ProbabilityBusiness business = new ProbabilityBusiness(
				GoodPuntsServices.getSpringRacingDAO(),
				GoodPuntsServices.getPuntingDAO(),
				SpringRacingServices.getStatistics(), 
				SpringRacingServices.getSimulator(), 
				m);
			List<Meeting> upcoming = business.fetchUpcomingMeets();
			mb.sortMeetingsByDate(upcoming);
			DecimalFormat df = new DecimalFormat("0.0");
			for (Meeting meet : upcoming) {
				//if (meet.getDate().getTime() > System.currentTimeMills() - 24 * 60 * 60 * 1000) { %>
					<tr><td colspan="6"><%=meet.getDate() %> <%=meet.getVenue() %></td></tr>		
<%					List<Punt> punts = GoodPuntsServices.getPuntingDAO().fetchPuntsForMeet(meet);
					for (Punt punt : punts) {%>
					<tr>
						<td>&nbsp;</td>
						<td>
							<a href="race_probabilities.jsp?race_code=<%=punt.getRace().getRaceCode()%>">
							Race <%=punt.getRace().getRaceNumber()%>
							</a>
						</td>
						<td><%=punt.getType()%></td>
						<td>
<% 						for (Runner horse : punt.getRunners()) {%>
							<%= horse.getNumber() + ".&nbsp;" + horse.getHorse() %>
<% 						}%>
						</td>
						<td><%=df.format(punt.getJoesOdds())%></td>
						<td><%=punt.getBookieOdds()%></td>
					</tr>
<%					}
				//}
			}
%>			</tbody>
		</table>
</body>
</html>
<%-- //[END all]--%>