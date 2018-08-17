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
				<p style="margin-left:40px">
					<%=r.getRaceDate() %>&nbsp;
					<%=r.getVenueName()%>&nbsp;
					<%=r.getPosition() %>&nbsp;
					<%=r.getPrizeMoney() %>&nbsp;
					<%=r.getWeight() %>&nbsp;
<%					if (r.getSplits() != null) {
						for (Double split : r.getSplits()) { %>
							<%=split %>&nbsp;
<%						}
					}
					%>
				</p>
<% 			}%>
<!-- @TODO Need to be able to get upcoming races for a horse -->

</body>
</html>
<%-- //[END all]--%>