package org.bell.framework;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.bell.app.BellRingJob;
import org.bell.dao.SchoolBellDao;
import org.bell.entity.FileNameConstants;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;


public class SchedulerUtil {

    private static Scheduler scheduler;

    public static void configure() throws SchedulerException {
        JobDetail job = JobBuilder.newJob(BellRingJob.class)
                .withIdentity("BellRingJobName", "BellRingGroup")
                .build();

        SimpleTrigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("BellRingTrigger", "BellRingGroup")
                .withSchedule(SimpleScheduleBuilder
                        .repeatMinutelyForever()
                        .withIntervalInSeconds(60)
                        .repeatForever())
                .startAt(Date.from(Instant.now()))
                .build();
        scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.getContext().put("dao", new SchoolBellDao());
        Media media = new Media(Paths.get(FileNameConstants.MP3_FILE_NAME).toUri().toString());
        scheduler.getContext().put("mp", new MediaPlayer(media));
        scheduler.scheduleJob(job, trigger);
    }

    public static void start() throws SchedulerException {
        if (!scheduler.isStarted()) {
            scheduler.start();
            System.out.println("Scheduler başladı");
        }
    }

    public static void pause() throws SchedulerException {
//        if (!scheduler.isInStandbyMode())
            scheduler.standby();
    }

    public static void shutdown() throws SchedulerException {
//        if (scheduler.isStarted())
            scheduler.shutdown();
    }

}
