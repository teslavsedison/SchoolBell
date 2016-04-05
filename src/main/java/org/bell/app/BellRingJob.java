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

    SchoolBellDao dao = null;
    private AdvancedPlayer advancedPlayer;

    public BellRingJob() {

    }

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
            // Tekrar Cuma ya çevrilecek
            case FRIDAY:
                schoolDay = dao.getBellTimesByGivenDay(DayName.FRIDAY);
                break;
        }

        if (schoolDay != null && schoolDay.getBellTimes() != null) {
            for (BellTime bellTime : schoolDay.getBellTimes()) {
                //                    Timer timer = new Timer();
//                    TimerTask ts = new TimerTask() {
//                        @Override
//                        public void run() {
//                            mediaPlayer.close();
//                            mediaPlayer = null;
//                            timer.cancel();
//                            timer.purge();
//                        }
//                    };
//
//                    timer.schedule(ts, 0, 20);
                if (bellTime.getTime().getHour() == LocalTime.now().getHour() && bellTime.getTime().getMinute() == LocalTime.now().getMinute()) {

//                    Thread thread = new PlayerTask(mediaPlayer);
//                    thread.start();
                    Timer tmr = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            advancedPlayer.close();
//                           // this.cancel();
//                            tmr.cancel();
//                            tmr.purge();
                        }
                    };

                    tmr.schedule(timerTask, 20 * 1000, 0);
                    try {
                        advancedPlayer.play();
                    } catch (JavaLayerException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}

class PlayerTask extends Thread {

    private AdvancedPlayer advancedPlayer;

    public PlayerTask(AdvancedPlayer advancedPlayer) {

        this.advancedPlayer = advancedPlayer;
    }

    @Override
    public void run() {

        while (!this.isInterrupted()) {
            try {
                final boolean[] counter = {false};
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (counter[0]) {
                            advancedPlayer.close();
                        }
                        counter[0] = true;
                    }
                };
                Timer tmr = new Timer();
                tmr.schedule(timerTask, 0, 20 * 1000);
                advancedPlayer.play();

//                LocalTime tme = LocalTime.now();
//                int second = tme.getSecond();
//                while (true) if () {
//                    this.interrupt();
//                    break;
//                }
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        }
    }
}

