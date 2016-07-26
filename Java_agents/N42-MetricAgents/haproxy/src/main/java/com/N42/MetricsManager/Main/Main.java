package com.N42.MetricsManager.Main;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;


public class Main {
	private static final Logger logger = Logger.getLogger(Main.class);
	public static void main(String[] args) {
		try {
			PropertyConfigurator.configure("config/log4j.properties");
			runHAProxyJob();

		} catch (Exception ex) {
		}
	}

	private static SimpleTrigger getTrigger() {
		SimpleTrigger metricsTrigger = new SimpleTrigger();
		MetricsManager manager = new MetricsManager();
		metricsTrigger.setName("metricsTrigger");
		metricsTrigger.setStartTime(new Date(System.currentTimeMillis() + 1000));
		metricsTrigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
		metricsTrigger.setRepeatInterval(manager.getTime_interval());
		return metricsTrigger;
	}

	
	private static void runHAProxyJob() {
		try {
			// configure the scheduler time
			SimpleTrigger metricsTrigger = getTrigger();
			JobDetail haproxyjob = new JobDetail();
			haproxyjob.setName("haproxyjob");
			haproxyjob.setJobClass(HAProxyManager.class);
			Scheduler haproxyScheduler = new StdSchedulerFactory().getScheduler();
			haproxyScheduler.scheduleJob(haproxyjob, metricsTrigger);
			haproxyScheduler.start();
		} catch (Exception ex) {
			logger.error("unable to start the scheduler due to this exception ::"+ex.toString());
		}
	}
}
