package org.bell.entity;

import java.time.LocalTime;
import java.util.ArrayList;

public class SchoolDay {
    private String dayName;

    private ArrayList<LocalTime> bellTimes;

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public ArrayList<LocalTime> getBellTimes() {
        return bellTimes;
    }

    public void setBellTimes(ArrayList<LocalTime> bellTimes) {
        this.bellTimes = bellTimes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getDayName());
        for (LocalTime time :
                bellTimes) {
            sb.append("\n");
            sb.append(time);
        }
        return sb.toString();
    }
}
