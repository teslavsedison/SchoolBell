package org.bell.framework;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.bell.dao.SchoolBellDao;
import org.bell.entity.DayName;
import org.bell.entity.FileNameConstants;
import org.bell.entity.SchoolDay;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class BellControl {
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    StartPlay startPlay;
    StopPlay stopPlay;

    public void start() throws JavaLayerException, IOException {
        AdvancedPlayer advancedPlayer = new AdvancedPlayer(Files.newInputStream(new File(FileNameConstants.MP3_FILE_NAME).toPath()));
        startPlay = new StartPlay(advancedPlayer);
        scheduler.scheduleAtFixedRate(startPlay, 0, 60, SECONDS);
        stopPlay = new StopPlay(advancedPlayer);
        scheduler.schedule(stopPlay, 20, SECONDS);
    }

    public void stop() {
        scheduler.shutdownNow();
//        scheduler.awaitTermination(0, MILLISECONDS);
        startPlay = null;
        stopPlay = null;
    }
}

class StartPlay implements Runnable {

    private AdvancedPlayer advancedPlayer;

    public StartPlay(AdvancedPlayer advancedPlayer) {

        this.advancedPlayer = advancedPlayer;
    }

    @Override
    public void run() {
        SchoolDay schoolDay = null;
        SchoolBellDao dao = new SchoolBellDao();

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
            case FRIDAY:
                schoolDay = dao.getBellTimesByGivenDay(DayName.FRIDAY);
                break;
        }

        if (schoolDay != null && schoolDay.getBellTimes() != null) {
            schoolDay.getBellTimes()
                    .stream()
                    .filter(bellTime -> bellTime.getTime().getHour() == LocalTime.now().getHour()
                            && bellTime.getTime().getMinute() == LocalTime.now().getMinute())
                    .distinct()
                    .forEach(bellTime -> {
                        try {
                            advancedPlayer.play();
                        } catch (JavaLayerException e) {
                            e.printStackTrace();
                        }

                    });
        }
    }
}

class StopPlay implements Runnable {
    private AdvancedPlayer advancedPlayer;

    public StopPlay(AdvancedPlayer advancedPlayer) {
        this.advancedPlayer = advancedPlayer;
    }

    @Override
    public void run() {
        advancedPlayer.stop();
    }
}