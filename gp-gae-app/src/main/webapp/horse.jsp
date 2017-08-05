<%-- //[START all]--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- //[START imports]--%>
<%@ page import="java.util.List" %>
<%@ page import="com.joe.springracing.SpringRacingServices" %>
<%@ page import="com.joe.springracing.objects.RunnerResult" %>
<%@ page import="com.joe.springracing.objects.Horse" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.List" %>
 
<%-- //[END imports]--%>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
</head>

<body>
	<jsp:include page="menu.jsp" />

<% 			String horseCode = request.getParameter("horse_code");
			Horse horse = SpringRacingServices.getSpringRacingDAO().fetchHorse(horseCode); %>
						
			<p><%=horse.getName() %></p>					
<%			List<RunnerResult> results =  horse.getPastResults();%>
<% 			for (RunnerResult r : results) {%>
				<p style="margin-left:40px">
					<%=r.getRaceDate() %>&nbsp;
					<%=r.getVenueName()%>&nbsp;
					<%=r.getPosition() %>&nbsp;
					<%=r.getPrizeMoney() %>&nbsp;
					<%=r.getWeight() %>&nbsp;
				</p>
<% 			}%>
<!-- @TODO Need to be able to get upcoming races for a horse -->

</body>
</html>
<%-- //[END all]--%>