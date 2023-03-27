<%-- //[START all]--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- //[START imports]--%>
<%@ page import="java.util.List" %>
<%@ page import="com.joe.springracing.business.HorseBusiness" %>
<%@ page import="com.joe.springracing.objects.RunnerResult" %>
<%@ page import="com.joe.springracing.objects.Horse" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.Calendar" %>
 
<%-- //[END imports]--%>
<% 			String horseCode = request.getParameter("id");
			HorseBusiness business = new HorseBusiness();
			Horse horse = business.fetchHorse(horseCode);
			if (horse == null)
				return;
			List<RunnerResult> results =  horse.getPastResults();
			
			for (RunnerResult r : results) {
				if (r != null && r.getOddsStart() != 0) {%>
			    <%=r.getHorse() %>,
			    <%=horse.getAveragePrizeMoney() %>,
			    <%=horse.getPrizeMoney() %>,
			    <%=horse.getColour() %>,
			    <%=horse.getAge() %>,
			    <%=horse.getSex() %>,
				<% if (r.getRaceDate() != null) {%><%=r.getRaceDate().getTime() %>
				<%} else {%><%=-1 %><%} %>,
				<%=r.getVenueName()%>,
				<%=r.getBarrier() %>,
				<%=r.getWeight() %>,
				<%=r.getRating() %>,
				<%=r.getJockey()%>,
				<%=r.getTrainer()%>,
				<%=r.getDistance()%>,
				<%=r.getTrackCondition()%>,<%=r.isTrial() %>,
				<%=r.getRacePrizeMoney() %>,<%=r.isScratched() %>,<%=r.isFirstUp() %>,<%=r.isSecondUp() %>,<%=r.isThirdUp() %>,<%=r.isFourthUp() %>
				<%=r.getOddsStart() %>,
				<%=r.getPosition() == 1? 0 : r.getMargin() %>
				<br />
<%				}
			}%>