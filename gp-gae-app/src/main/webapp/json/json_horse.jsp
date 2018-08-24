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
			List<RunnerResult> results =  horse.getPastResults();
			
			for (RunnerResult r : results) {%>
			    <%=r.getHorse() %>,
			    <%=horse.getAveragePrizeMoney() %>,
			    <%=horse.getPrizeMoney() %>,
			    <%=horse.getColour() %>,
			    <%=horse.getAge() %>,
			    <%=horse.getSex() %>,
				<%=r.getRaceDate() %>,
				<%=System.currentTimeMillis() - r.getRaceDate().getTime() %>,
				<%=r.getVenueName()%>,
				<%=r.getBarrier() %>,
				<%=r.getWeight() %>,
				<%=r.getRating() %>,
				<%=r.getJockey()%>,
				<%=r.getTrainer()%>,
				<%=r.getDistance()%>,
				<%=r.getTrackCondition()%>,
				<%=r.isTrial() %>,
				<%=horse.isGoodAtClass()%>,
				<%=horse.isGoodAtDistance()%>,
				<%=horse.isGoodAtTrack()%>,
				<%=horse.isGoodAtTrackCondition()%>,
				<%=r.getRacePrizeMoney() %>,
				<%=r.getPosition() %>,
				<%=r.getPrizeMoney() %>,
				<%=r.getRaceTime() %>,
				<%=r.isScratched() %>,		
<%				if (r.getSplits() != null) {
					for (Double split : r.getSplits()) { %>
						<%=split %>,
<%					}
				}%><br />
<%			}%>