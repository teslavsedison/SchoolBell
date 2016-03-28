package org.bell.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "SchoolDay")
public class SchoolDay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SchoolDayId", unique = true, nullable = false)
    private int id;

    @Column(name = "DayName", length = 100)
    private String dayName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "schoolDay", targetEntity = BellTime.class)
    @Cascade({CascadeType.MERGE, CascadeType.DELETE, CascadeType.PERSIST})
    private List<BellTime> bellTimes = new ArrayList<>();

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
