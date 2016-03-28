package org.bell.entity;

import javax.persistence.*;
import java.time.LocalTime;

/**
 * Created by hkn on 17.03.2016.
 */

@Entity()
@Table(name = "BellTime")
public class BellTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BellTimeId", unique = true, nullable = false)
    private int id;

    @Column(name = "BTime")
    private LocalTime time;

    @Column(name = "Description", length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SchoolDayId", nullable = false)
    private SchoolDay schoolDay;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return time.toString();
    }


    public SchoolDay getSchoolDay() {
        return schoolDay;
    }

    public void setSchoolDay(SchoolDay schoolDay) {
        this.schoolDay = schoolDay;
    }
}

