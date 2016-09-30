package org.bell.app;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.bell.dao.SchoolBellDao;
import org.bell.entity.BellTime;
import org.bell.entity.DayName;
import org.bell.entity.SchoolDay;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

//@DisallowConcurrentExecution
public class BellRingJob implements Job {

    private static AdvancedPlayer advancedPlayer;
    private SchoolBellDao dao = null;

    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        System.out.println(LocalTime.now().toString());
        SchoolDay schoolDay = null;
        try {
            advancedPlayer = (AdvancedPlayer) context.getScheduler().getContext().get("mp");
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
            case FRIDAY:
                schoolDay = dao.getBellTimesByGivenDay(DayName.FRIDAY);
                break;
        }

        if (schoolDay != null && schoolDay.getBellTimes() != null) {
            for (BellTime bellTime : schoolDay.getBellTimes()) {
                if (bellTime.getTime().getHour() == LocalTime.now().getHour() && bellTime.getTime().getMinute() == LocalTime.now().getMinute()) {
                    Timer tmr = new Timer();
                    PlayerTask playerTask = new PlayerTask(advancedPlayer);
                    playerTask.start();
                    PlayerStopTask playerStopTask = new PlayerStopTask(playerTask, advancedPlayer);
                    tmr.schedule(playerStopTask, 0, 20 * 1000);

                }

            }
        }
    }
}

class PlayerStopTask extends TimerTask {

    private PlayerTask playerTask;
    private AdvancedPlayer advancedPlayer;

    public PlayerStopTask(PlayerTask playerTask, AdvancedPlayer advancedPlayer) {

        this.playerTask = playerTask;
        this.advancedPlayer = advancedPlayer;
    }

    @Override
    public synchronized void run() {
        System.out.println("Stop task started");
        advancedPlayer.close();
        playerTask.interrupt();
        this.cancel();
    }
}


class PlayerTask extends Thread {

    private AdvancedPlayer advancedPlayer;

    public PlayerTask(AdvancedPlayer advancedPlayer) {

        this.advancedPlayer = advancedPlayer;
    }

    @Override
    public synchronized void run() {

        while (!this.isInterrupted()) {
            try {
                System.out.println("Player started");
                advancedPlayer.play();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        }
    }
}

