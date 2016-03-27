import org.bell.app.DailyBellCalculator;
import org.bell.entity.BellTime;
import org.bell.entity.SchoolDay;
import org.bell.framework.HibernateUtil;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalTime;
import java.util.List;

/**
 * Created by hkn on 18.03.2016.
 */

public class BellModelTest {

    @Test
    public void calculateTimesTest() {
        DailyBellCalculator bellModel = getDailyBellCalculator();
        List<BellTime> localTimes = bellModel.calculateBellTime();
        for (BellTime t : localTimes) {
            System.out.println(t.toString());
        }
    }

    private DailyBellCalculator getDailyBellCalculator() {
        DailyBellCalculator bellModel = new DailyBellCalculator();
        bellModel.setLectureTime(LocalTime.of(0, 40));
        bellModel.setStartTime(LocalTime.of(8, 0));
        bellModel.setBreakTime(LocalTime.of(0, 10));
        bellModel.setLectureCountBeforeLunch(5);
        bellModel.setLunchBreakTime(LocalTime.of(1, 0));
        bellModel.setLectureCountAfterLunch(4);
        return bellModel;
    }

    @Test
    public void saveSchoolDay() {

    }

    @Test
    public void getNotComputedSchoolDay() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List list = session.createQuery("from SchoolDay as sd where sd.bellTimes.size>0")
                .list();
        session.close();
        list.forEach(o -> System.out.println(o));
        list.forEach(o -> ((SchoolDay) o).getBellTimes().forEach(bellTime -> System.out.println(bellTime.getTime())));
        Assert.assertNotNull(list);
    }
}
