package org.bell.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SchoolDay")
public class SchoolDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SchoolDayId", unique = true, nullable = false)
    private int id;

    @Column(name = "DayName")
    private String dayName;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy = "schoolDay")
    private List<BellTime> bellTimes = new ArrayList<>(0);

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public List<BellTime> getBellTimes() {
        return bellTimes;
    }

    public void setBellTimes(List<BellTime> bellTimes) {
        this.bellTimes = bellTimes;
    }

    @Override
    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(getDayName());
//        if (bellTimes != null) {
//            for (BellTime time : bellTimes) {
//                sb.append("\n");
//                sb.append(time.getTime());
//            }
//        }
//        return sb.toString();
        return this.getDayName();
    }
}
