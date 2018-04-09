<%-- //[START all]--%>
<%@ page contentType="text/json;charset=UTF-8" language="java" %>
<%-- //[START imports]--%>
<%@ page import="java.util.List" %>
<%@ page import="com.joe.springracing.SpringRacingServices" %>
<%@ page import="com.joe.springracing.objects.Race" %>
<%-- //[END imports]--%>
{"races":[<%List<Race> races = SpringRacingServices.getSpringRacingDAO().fetchRacesWithResults(); 
  int i = 0;
  for (i = 0; i < races.size() - 1; i++) {
  	Race race = races.get(i);%>
    <%=race.getRaceCode() %>,
<%}
  Race race = races.get(i);%>
  <%=race.getRaceCode() %>
]}
<%-- //[END all]--%>