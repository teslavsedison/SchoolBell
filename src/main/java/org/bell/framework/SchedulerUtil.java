package org.bell.framework;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.bell.app.BellRingJob;
import org.bell.dao.SchoolBellDao;
import org.bell.entity.FileNameConstants;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;


public class SchedulerUtil {

    private static Scheduler scheduler;
    private static boolean isConfigured = false;
    private static SimpleTrigger trigger;
    private static JobDetail job;

    public static void configure() throws SchedulerException, IOException {
        if (Files.exists(Paths.get(FileNameConstants.MP3_FILE_NAME))) {
            job = JobBuilder.newJob(BellRingJob.class)
                    .withIdentity("BellRingJobName", "BellRingGroup")
                    .build();

            trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("BellRingTrigger", "BellRingGroup")
                    .withSchedule(SimpleScheduleBuilder
                            .repeatMinutelyForever()
                            .withIntervalInSeconds(60)
                            .repeatForever())
                    .startAt(Date.from(Instant.now()))
//                    .startAt(Date.from(Instant.from(LocalDateTime.of(LocalDate.now().getYear(),
//                            LocalDate.now().getMonth(),
//                            LocalDate.now().getDayOfMonth(),
//                            LocalTime.now().getHour(),
//                            LocalTime.now().getMinute()).toInstant(ZoneOffset.UTC))))
                    .build();


            if (scheduler != null) {
                scheduler.clear();
                scheduler = null;
            }
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.getContext().put("dao", new SchoolBellDao());
            MediaPlayer player = null;
            Media media = new Media(Paths.get(FileNameConstants.MP3_FILE_NAME).toUri().toString());
            scheduler.getContext().put("mp", new MediaPlayer(media));
//        Media media = new Media(new File(FileNameConstants.MP3_FILE_NAME).toURI().toString());
//        scheduler.getContext().put("mp", new MediaPlayer(media));
            scheduler.getContext().put("mp", player);
            scheduler.start();
            isConfigured = true;
        } else {
            isConfigured = false;
        }
    }

    public static boolean isConfigured() {
        return isConfigured;
    }

    public static void start() throws SchedulerException {

        scheduler.scheduleJob(job, trigger);

//        if (!scheduler.isStarted() || scheduler.isInStandbyMode()) {
//            scheduler.start();
////            System.out.println("Scheduler başladı");
//        }
    }

    public static void pause() throws SchedulerException {
        scheduler.unscheduleJob(trigger.getKey());
//        if (scheduler.isStarted() || !scheduler.isInStandbyMode())
//            scheduler.standby();
    }

    public static void shutdown() throws SchedulerException {
        if (scheduler != null)
            scheduler.shutdown(true);
    }

}


