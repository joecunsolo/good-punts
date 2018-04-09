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
<%@ page import="com.joe.springracing.business.model.AnalysableObjectStatistic" %>
<%@ page import="com.joe.springracing.business.model.stats.SingleVariateStatistic" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.Comparator" %> 
<%-- //[END imports]--%>
<% 			String raceCode = request.getParameter("race_code");
			Race race = SpringRacingServices.getSpringRacingDAO().fetchRace(raceCode);
 			List<Runner> runners = SpringRacingServices.getPuntingDAO().fetchProbabilitiesForRace(race);
			int[] result = race.getResult();
			DecimalFormat df = new DecimalFormat("0.0");
			for (Runner runner : runners) {%>
				<%=race.getDate() %>,
				<%=race.getRaceCode() %>,
				<%=race.getDistance() %>,
			    <%=race.getMaxRunnerNumber() %>,
			    <%=race.getVenue() %>,
			    <%=race.getPrizeMoney(1) %>,
			    <%=runner.getJockey() %>,
			    <%=runner.getTrainer() %>,
				<%=runner.getNumber()%>,
				<%=runner.getHorse() %>,
				<%=SpringRacingServices.getSpringRacingDAO().fetchHorse(runner.getHorse()).getPastResults().size()%>
				<%=df.format(runner.getProbability().getMean())%>,
				<%=df.format(runner.getProbability().getStandardDeviation())%>,
				<%=df.format(runner.getWeight())%>,
				<%=df.format(runner.getProbability().getWin() * 100)%>,
				<%=runner.getOdds().getWin()%>,
				<%=runner.isScratched()%>,
				<%=runner.isEmergency()%>,
				<%=runner.getBarrier() %>,
				<%=runner.isGoodAtDistance()%>,
				<%=runner.isGoodAtClass()%>,
				<%=runner.isGoodAtTrack()%>,
				<%=runner.isGoodAtTrackCondition()%>,
				<%if (result != null) {
					for (int i = 0; i < result.length; i++) {
						if (runner.getNumber() == result[i]) { %>
						<%=i+1 %>
					   <%}
					}%>
<%				} else {%>
					<%=-1 %>
<% 				}%>
				<br>
<%		}%>