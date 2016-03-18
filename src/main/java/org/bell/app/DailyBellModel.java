package org.bell.app;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DailyBellModel {

    private LocalTime startTime;
    private LocalTime lectureTime;// = new SimpleStringProperty();
    private LocalTime breakTime;// = new SimpleStringProperty();
    private int lectureCountBeforeLunch;// = new SimpleIntegerProperty();
    private LocalTime lunchBreakTime;// = new SimpleStringProperty();
    private int lectureCountAfterLunch;// = new SimpleIntegerProperty();


    private List<LocalTime> localTimes = new ArrayList<>();

    public List<LocalTime> calculateBellTime() {
        for (int beforeIndex = 0; beforeIndex <= lectureCountBeforeLunch; beforeIndex++) {
            if (beforeIndex == 0)
                localTimes.add(startTime);
            else {
                LocalTime tempLectureTime = localTimes.get(localTimes.size() - 1).plusMinutes(lectureTime.getMinute());
                LocalTime tempBreakThreeMinTime = tempLectureTime.plusMinutes(getBreakTime().getMinute() - 3);
                LocalTime tempBreakTime = tempBreakThreeMinTime.plusMinutes(getBreakTime().getMinute() - 7);
                localTimes.add(tempLectureTime);
                if (beforeIndex != lectureCountBeforeLunch) {
                    localTimes.add(tempBreakThreeMinTime);
                    localTimes.add(tempBreakTime);
                }
            }
        }
        for (int afterIndex = 0; afterIndex <= lectureCountAfterLunch; afterIndex++) {
            if (afterIndex == 0) {
                int min = lunchBreakTime.getMinute() + lunchBreakTime.getHour() * 60;
                LocalTime localTime = localTimes.get(localTimes.size() - 1).plusMinutes(min);
                localTimes.add(localTime);
            } else {
                LocalTime tempLectureTime = localTimes.get(localTimes.size() - 1).plusMinutes(lectureTime.getMinute());
                LocalTime tempBreakThreeMinTime = tempLectureTime.plusMinutes(getBreakTime().getMinute() - 3);
                LocalTime tempBreakTime = tempBreakThreeMinTime.plusMinutes(getBreakTime().getMinute() - 7);
                localTimes.add(tempLectureTime);
                if (afterIndex != lectureCountAfterLunch) {
                    localTimes.add(tempBreakThreeMinTime);
                    localTimes.add(tempBreakTime);
                }
            }
        }
        return localTimes;
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
