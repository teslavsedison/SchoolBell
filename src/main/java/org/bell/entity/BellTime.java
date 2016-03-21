package org.bell.entity;

import java.time.LocalTime;

/**
 * Created by hkn on 17.03.2016.
 */


public class BellTime {

    private LocalTime time;
    private String description;

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        if (description != null && time != null)
            return description + " " + time.toString();
        return "";
    }
}

