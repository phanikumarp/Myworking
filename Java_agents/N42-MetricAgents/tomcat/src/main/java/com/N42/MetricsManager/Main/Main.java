package com.N42.MetricsManager.Main;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

public class Main  {
	private static final Logger logger = Logger.getLogger(Main.class);
	private static SimpleTrigger metricsTrigger;
	public Main() {
		
	}
	public static void main(String[] args) {
		try {
			PropertyConfigurator.configure("config/log4j.properties");
			runTomcatJob();
		} catch (Exception ex) {
		}
	}

	private static SimpleTrigger getTrigger() throws IOException {
		if (null == metricsTrigger){
			metricsTrigger = new SimpleTrigger();
			TomcatMetricManager manager = new TomcatMetricManager();
			metricsTrigger.setName("metricsTrigger");
			metricsTrigger.setStartTime(new Date(System.currentTimeMillis() + 1000));
			metricsTrigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
			metricsTrigger.setRepeatInterval(manager.getTimeInterval());
			
		}
		return metricsTrigger;
	}

	private static void runTomcatJob() throws SchedulerException {
		JobDetail tomcatjob =null;
		Scheduler tomcatScheduler = null;
		try {
			// configure the scheduler time
			logger.debug("Configuring the schedular time");
			SimpleTrigger metricsTrigger = getTrigger();
           if(null == tomcatjob){
			tomcatjob = new JobDetail();
			tomcatjob.setName("tomcatjob");
			tomcatjob.setJobClass(TomcatMetricManager.class);
			tomcatScheduler = new StdSchedulerFactory().getScheduler();
			tomcatScheduler.scheduleJob(tomcatjob, metricsTrigger);
			tomcatScheduler.start();
           }
		} catch (Exception ex) {
			 logger.error("unable to start the scheduler due to this exception ::"+ex.toString());
		}
		finally{
			  
		     tomcatjob=null;
		 
		}
	}
}
