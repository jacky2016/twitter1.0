package com.xunku.app.jobs;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.xunku.app.AppContext;

public class JobFactory {

	/**
	 * 创建一个JOB
	 * 
	 * @param context
	 * @param jobClass
	 * @param jobName
	 * @param jobGroup
	 * @param jobTrigger
	 * @param repeatSeconds
	 * @return
	 * @throws SchedulerException
	 */
	public static Scheduler buildJob(AppContext context,
			Class<? extends Job> jobClass, String jobName, String jobGroup,
			String jobTrigger, int repeatSeconds) throws SchedulerException {

		Scheduler scheduler = new StdSchedulerFactory().getScheduler();

		JobDetail job = JobBuilder.newJob(jobClass).withIdentity(jobName,
				jobGroup).build();

		job.getJobDataMap().put("context", context);

		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobTrigger,
				jobGroup).startAt(new Date()).withSchedule(
				SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(
						repeatSeconds).repeatForever()).build();

		scheduler.scheduleJob(job, trigger);

		return scheduler;

	}

}
