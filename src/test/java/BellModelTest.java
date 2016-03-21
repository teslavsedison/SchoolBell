import org.bell.app.DailyBellCalculator;
import org.junit.Test;

import java.time.LocalTime;
import java.util.List;

/**
 * Created by hkn on 18.03.2016.
 */

public class BellModelTest {

    @Test
    public void calculateTimesTest() {
        DailyBellCalculator bellModel = new DailyBellCalculator();
        bellModel.setLectureTime(LocalTime.of(0, 40));
        bellModel.setStartTime(LocalTime.of(8, 0));
        bellModel.setBreakTime(LocalTime.of(0, 10));
        bellModel.setLectureCountBeforeLunch(5);
        bellModel.setLunchBreakTime(LocalTime.of(1, 0));
        bellModel.setLectureCountAfterLunch(4);
        List<LocalTime> localTimes = bellModel.calculateBellTime();
        for (LocalTime t : localTimes) {
            System.out.println(t.toString());
        }
    }
}
