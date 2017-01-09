package com.example.guestbook;

import com.goodpunts.objectify.ObjHorse;
import com.goodpunts.objectify.ObjMeet;
import com.goodpunts.objectify.ObjOdds;
import com.goodpunts.objectify.ObjProbability;
import com.goodpunts.objectify.ObjPunt;
import com.goodpunts.objectify.ObjRace;
import com.goodpunts.objectify.ObjRunner;
import com.goodpunts.objectify.ObjRunnerResult;
import com.goodpunts.objectify.ObjStatistic;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

/**
 * OfyHelper, a ServletContextListener, is setup in web.xml to run before a JSP is run.  This is
 * required to let JSP's access Ofy.
 **/
public class OfyHelper implements ServletContextListener {
  public void contextInitialized(ServletContextEvent event) {
    // This will be invoked as part of a warmup request, or the first user request if no warmup
    // request.
    ObjectifyService.register(Guestbook.class);
    ObjectifyService.register(Greeting.class);
    ObjectifyService.register(ObjMeet.class);
    ObjectifyService.register(ObjRace.class);
    ObjectifyService.register(ObjRunner.class);
    ObjectifyService.register(ObjOdds.class);
    ObjectifyService.register(ObjHorse.class);
    ObjectifyService.register(ObjRunnerResult.class);
    ObjectifyService.register(ObjPunt.class);
    ObjectifyService.register(ObjProbability.class);
    ObjectifyService.register(ObjStatistic.class);
  }

  public void contextDestroyed(ServletContextEvent event) {
    // App Engine does not currently invoke this method.
  }
}