package org.bell.app;

import java.time.LocalTime;
import java.util.ArrayList;

public class DailyBellCalculator {

    private LocalTime startTime;
    private LocalTime lectureTime;// = new SimpleStringProperty();
    private LocalTime breakTime;// = new SimpleStringProperty();
    private int lectureCountBeforeLunch;// = new SimpleIntegerProperty();
    private LocalTime lunchBreakTime;// = new SimpleStringProperty();
    private int lectureCountAfterLunch;// = new SimpleIntegerProperty();


    private ArrayList<LocalTime> localTimes = new ArrayList<>();

    public ArrayList<LocalTime> calculateBellTime() {
        for (int beforeIndex = 0; beforeIndex <= lectureCountBeforeLunch; beforeIndex++) {
            if (beforeIndex == 0) {
                localTimes.add(startTime);
            }
            else {
                computeBellTimes(beforeIndex, lectureCountBeforeLunch);
            }
        }
        for (int afterIndex = 0; afterIndex <= lectureCountAfterLunch; afterIndex++) {
            if (afterIndex == 0) {
                int min = lunchBreakTime.getMinute() + lunchBreakTime.getHour() * 60;
                LocalTime localTime = localTimes.get(localTimes.size() - 1).plusMinutes(min);
                localTimes.add(localTime);
            } else {
                computeBellTimes(afterIndex, lectureCountAfterLunch);
            }
        }
        return localTimes;
    }

    private void computeBellTimes(int index, int lectureCount) {
        LocalTime tempLectureTime = localTimes.get(localTimes.size() - 1).plusMinutes(lectureTime.getMinute());
        LocalTime tempBreakThreeMinTime = tempLectureTime.plusMinutes(getBreakTime().getMinute() - 3);
        LocalTime tempBreakTime = tempBreakThreeMinTime.plusMinutes(getBreakTime().getMinute() - 7);
        localTimes.add(tempLectureTime);
        if (index != lectureCount) {
            localTimes.add(tempBreakThreeMinTime);
            localTimes.add(tempBreakTime);
        }
    }

    public LocalTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setLectureTime(LocalTime lectureTime) {
        this.lectureTime = lectureTime;
    }

    public LocalTime getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(LocalTime breakTime) {
        this.breakTime = breakTime;
    }

    public void setLectureCountBeforeLunch(int lectureCountBeforeLunch) {
        this.lectureCountBeforeLunch = lectureCountBeforeLunch;
    }

    public LocalTime getLunchBreakTime() {
        return lunchBreakTime;
    }

    public void setLunchBreakTime(LocalTime lunchBreakTime) {
        this.lunchBreakTime = lunchBreakTime;
    }

    public int getLectureCountAfterLunch() {
        return lectureCountAfterLunch;
    }

    public void setLectureCountAfterLunch(int lectureCountAfterLunch) {
        this.lectureCountAfterLunch = lectureCountAfterLunch;
    }
}
