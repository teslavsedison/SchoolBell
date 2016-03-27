package org.bell.framework;

import org.bell.app.BellRingJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.time.Instant;
import java.util.Date;

public class SchedulerUtil {
    private static JobDetail job = null;
    private static Trigger trigger = null;
    private static Scheduler scheduler = null;

    public static void configure() throws SchedulerException {
        job = JobBuilder.newJob(BellRingJob.class)
                .withIdentity("BellRingJobName", "BellRingGroup").build();

        trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("BellRingTrigger", "BellRingGroup")
                .withSchedule(SimpleScheduleBuilder
                        .repeatMinutelyForever()
                        .withIntervalInSeconds(60)
                        .repeatForever())
                .startAt(Date.from(Instant.now()))
                .build();
        scheduler = new StdSchedulerFactory().getScheduler();
    }

    public static void start() throws SchedulerException {
        if (!scheduler.isStarted()) {
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        }
    }

    public static void pause() throws SchedulerException {
        if (!scheduler.isInStandbyMode())
            scheduler.standby();
    }

    public static void shutdown() throws SchedulerException {
        if (scheduler.isStarted())
            scheduler.shutdown();
    }

}
