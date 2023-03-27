<%-- //[START all]--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- //[START imports]--%>
<%@ page import="java.util.List" %>
<%@ page import="com.joe.springracing.business.MeetBusiness" %>
<%@ page import="com.joe.springracing.business.HorseBusiness" %>
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
<% 		
			String raceCode = request.getParameter("race_code");
			Race race = SpringRacingServices.getSpringRacingDAO().fetchRace(raceCode);
			int[] result = race.getResult();
 			List<Runner> runners = SpringRacingServices.getPuntingDAO().fetchProbabilitiesForRace(race);
			DecimalFormat df = new DecimalFormat("0.0");
			HorseBusiness hb = new HorseBusiness();
			for (Runner runner : runners) {
				if (runner.getOdds().getWin() != 0) {
				  Horse horse = hb.fetchHorse(runner.getHorse());
			%>	
				  <%=runner.getHorse() %>,
				  <%=horse.getAveragePrizeMoney() %>,
			      <%=horse.getPrizeMoney() %>,
			      <%=horse.getColour() %>,
			      <%=horse.getAge() %>,
			      <%=horse.getSex() %>,
				  <% if (race.getDate() != null) {%><%=race.getDate().getTime() %>
				  <%} else {%><%=-1 %><%} %>
				  <%=race.getVenue() %>,
			      <%=runner.getBarrier() %>,
			      <%=df.format(runner.getWeight())%>,
				  <%=runner.getRating() %>,
				  <%=runner.getJockey() %>,
			      <%=runner.getTrainer() %>,
				  <%=race.getDistance() %>,
				  <%=race.getTrackCondition() %>,<%=race.isTrial() %>,				
				  <%=race.getRacePrizeMoney() %>,<%=runner.isScratched()%>,
			      <%=runner.getOdds().getWin() %>
				<br>
<%				}
			}%>