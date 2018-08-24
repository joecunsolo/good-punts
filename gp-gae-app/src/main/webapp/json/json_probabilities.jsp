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
				Horse horse = hb.fetchHorse(runner.getHorse());
				int position = -1;
				if (result != null) {
					for (int i = 0; i < result.length; i++) {
						if (runner.getNumber() == result[i]) { 
							position = i+1;
					   }
					}
 				}
			%>	
				<%=runner.getHorse() %>,
				<%=horse.getAveragePrizeMoney() %>,
			    <%=horse.getPrizeMoney() %>,
			    <%=horse.getColour() %>,
			    <%=horse.getAge() %>,
			    <%=horse.getSex() %>,
				<%=race.getDate() %>,
				0,
				<%=race.getVenue() %>,
			    <%=runner.getBarrier() %>,
			    <%=df.format(runner.getWeight())%>,
				<%=runner.getRating() %>,
				<%=runner.getJockey() %>,
			    <%=runner.getTrainer() %>,
				<%=race.getDistance() %>,
				<%=race.getTrackCondition() %>,
			    <%=horse.isGoodAtClass() %>,
				<%=horse.isGoodAtDistance() %>,
				<%=horse.isGoodAtTrack() %>,
				<%=horse.isGoodAtTrackCondition() %>,
				<%=race.isTrial() %>,				
				<%=race.getRacePrizeMoney() %>,
				<%//Position,PrizeMoney,RaceTime,Scratched,200m,400m,600m,800m,1000m,1200m,1400m,1600m,1800m,2000m,2200m,2400m,2600m,2800m,3000m,3200m%>
				<%=position %>,
				<%=race.getPrizeMoney(position) %>,
				0,
			    <%=runner.isScratched()%>,
				<%if (race.getSplits() != null) {
				  	List<Double> splits = race.getSplits().get(runner.getNumber());
				  	if (splits != null) {
				  		for (Double split : splits) { %>
						<%=split %>,
				<%		}
				  	}
				  }%>
				<br>
<%		}%>