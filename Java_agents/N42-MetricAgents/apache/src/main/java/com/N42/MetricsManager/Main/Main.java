package com.N42.MetricsManager.Main;

import java.util.Date;
//import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;


public class Main {
//	  protected volatile Map<String, String> oldValueMap;
//	  protected volatile Map<String, String> valueMap;
//	  protected volatile long oldTime = 0L;
//	  protected volatile long currentTime = 0L;
	private static final Logger logger = Logger.getLogger(Main.class);
	public static void main(String[] args) {
		try {
			PropertyConfigurator.configure("config/log4j.properties");
			runApacheJob();
			
		} catch (Exception ex) {
		}
	}

	private static SimpleTrigger getTrigger() {
		SimpleTrigger metricsTrigger = new SimpleTrigger();
		MetricsManager manager = new MetricsManager();
		metricsTrigger.setName("metricsTrigger");
		metricsTrigger.setStartTime(new Date(System.currentTimeMillis() + 1000));
		metricsTrigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
		metricsTrigger.setRepeatInterval(manager.getInterval_time());
		return metricsTrigger;
	}

	

	private static void runApacheJob() {
		try {
			// configure the scheduler time
			logger.debug("Configuring the schedular time");
			SimpleTrigger metricsTrigger = getTrigger();
			JobDetail apachejob = new JobDetail();
			apachejob.setName("apachejob");
			apachejob.setJobClass(ApacheManager.class);

			Scheduler apacheScheduler = new StdSchedulerFactory().getScheduler();
			apacheScheduler.scheduleJob(apachejob, metricsTrigger);
			apacheScheduler.start();
		} catch (Exception ex) {
			logger.error("unable to start the scheduler due to this exception ::"+ex.toString());
		}
	}
}

	
