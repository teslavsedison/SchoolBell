package org.bell.app;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.bell.dao.SchoolBellDao;
import org.bell.entity.BellTime;
import org.bell.entity.DayName;
import org.bell.entity.FileNameConstants;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@DisallowConcurrentExecution
public class BellRingJob implements Job {

    SchoolBellDao dao = null;
    MediaPlayer mediaPlayer;
    Media media;

    public BellRingJob() {
        dao = new SchoolBellDao();
        media = new Media(FileNameConstants.MP3_FILE_NAME);
        mediaPlayer = new MediaPlayer(media);

    }

    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        ArrayList<BellTime> belllTimes = null;
        switch (LocalDate.now().getDayOfWeek()) {
            case MONDAY:
                belllTimes = dao.getBellTimesByGivenDay(DayName.MONDAY);
                break;
            case TUESDAY:
                belllTimes = dao.getBellTimesByGivenDay(DayName.TUESDAY);
                break;
            case WEDNESDAY:
                belllTimes = dao.getBellTimesByGivenDay(DayName.WEDNESDAY);
                break;
            case THURSDAY:
                belllTimes = dao.getBellTimesByGivenDay(DayName.THURSDAY);
                break;
            case FRIDAY:
                belllTimes = dao.getBellTimesByGivenDay(DayName.FRIDAY);
                break;
        }

        if (belllTimes != null) belllTimes.forEach(bellTime -> {
            if (bellTime.getTime().getHour() == LocalTime.now().getHour() &&
                    bellTime.getTime().getMinute() == LocalTime.now().getMinute()) {
                mediaPlayer.play();
                mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue.toSeconds() == 30)
                        mediaPlayer.stop();
                });
            }
        });
    }
}

