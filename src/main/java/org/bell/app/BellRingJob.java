package org.bell.app;

import javafx.scene.media.MediaPlayer;
import org.bell.dao.SchoolBellDao;
import org.bell.entity.DayName;
import org.bell.entity.SchoolDay;
import org.quartz.*;

import java.time.LocalDate;
import java.time.LocalTime;

@DisallowConcurrentExecution
public class BellRingJob implements Job {

    SchoolBellDao dao = null;
    MediaPlayer mediaPlayer;

    public BellRingJob() {
//        dao = new SchoolBellDao();
//        media = new Media(FileNameConstants.MP3_FILE_NAME);
//        mediaPlayer = new MediaPlayer(media);
//        System.out.println("BellRingJob oluşturuldu");
    }

    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        System.out.println(LocalTime.now().toString());
        SchoolDay schoolDay = null;
        try {
            mediaPlayer = (MediaPlayer) context.getScheduler().getContext().get("mp");
            dao = (SchoolBellDao) context.getScheduler().getContext().get("dao");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        switch (LocalDate.now().getDayOfWeek()) {
            case MONDAY:
                schoolDay = dao.getBellTimesByGivenDay(DayName.MONDAY);
                break;
            case TUESDAY:
                schoolDay = dao.getBellTimesByGivenDay(DayName.TUESDAY);
                break;
            case WEDNESDAY:
                schoolDay = dao.getBellTimesByGivenDay(DayName.WEDNESDAY);
                break;
            case THURSDAY:
                schoolDay = dao.getBellTimesByGivenDay(DayName.THURSDAY);
                break;
            // Tekrar Cuma ya çevrilecek
            case SUNDAY:
                schoolDay = dao.getBellTimesByGivenDay(DayName.FRIDAY);
                break;
        }

        if (schoolDay != null && schoolDay.getBellTimes() != null) {
            schoolDay.getBellTimes().forEach(bellTime -> {
                if (bellTime.getTime().getHour() == LocalTime.now().getHour() &&
                        bellTime.getTime().getMinute() == LocalTime.now().getMinute()) {
                    mediaPlayer.play();
                    while (true) {
                        if (mediaPlayer.getCurrentTime().toSeconds() >= 20) {
                            mediaPlayer.stop();
                            break;
                        }
                    }
                }
            });
        }
    }
}

