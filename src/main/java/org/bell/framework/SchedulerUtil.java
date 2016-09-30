package org.bell.framework;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.bell.app.BellRingJob;
import org.bell.dao.SchoolBellDao;
import org.bell.entity.FileNameConstants;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Date;


public class SchedulerUtil {

    private static Scheduler scheduler;

    public static void configure() throws SchedulerException, IOException {
//        if (Files.exists(Paths.get(FileNameConstants.MP3_FILE_NAME))) {
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
        Player player = null;
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.getContext().put("dao", new SchoolBellDao());
            scheduler.scheduleJob(job, trigger);
    }

    public static void start() throws SchedulerException {
        AdvancedPlayer player = null;
        try {
            player = new AdvancedPlayer(Files.newInputStream(new File(FileNameConstants.MP3_FILE_NAME).toPath()));
        } catch (JavaLayerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Media media = new Media(new File(FileNameConstants.MP3_FILE_NAME).toURI().toString());
        //scheduler.getContext().put("mp", new MediaPlayer(media));
        scheduler.getContext().put("mp", player);
        if (!scheduler.isStarted() || scheduler.isInStandbyMode()) {
            scheduler.start();
//            System.out.println("Scheduler başladı");
        }
    }

    public static void pause() throws SchedulerException {
        if (scheduler.isStarted() || !scheduler.isInStandbyMode())
            scheduler.standby();
    }

    public static void shutdown() throws SchedulerException {
        if (scheduler != null && scheduler.isStarted())
            scheduler.shutdown(true);
    }

}


