package org.bell.app;

import impl.org.controlsfx.i18n.Localization;
import it.sauronsoftware.junique.AlreadyLockedException;
import it.sauronsoftware.junique.JUnique;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.bell.entity.DayName;
import org.bell.entity.SchoolDay;
import org.bell.framework.HibernateUtil;
import org.bell.framework.SchedulerUtil;
import org.hibernate.Session;

import java.util.Locale;

public class Main extends Application {

    public static void main(String[] args) {
        String appId = "SchoolBell";
        boolean alreadyRunning;
        try {
            JUnique.acquireLock(appId);
            alreadyRunning = false;
        } catch (AlreadyLockedException e) {
//            e.printStackTrace();
            alreadyRunning = true;
        }
        if (!alreadyRunning) {
//            LauncherImpl.launchApplication(Main.class, SchoolBellPreloader.class, args);
            launch(args);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setDb();
        SchedulerUtil.configure();
        Localization.setLocale(new Locale("tr-TR"));
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/MainView.fxml"));
        primaryStage.setTitle("Okul Zili");
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("bell1.png")));
        Scene scene = new Scene(root, 380, 520);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        //8SchoolBellPreloader.getPreloaderStage().close();
        primaryStage.show();
    }

    private void setDb() {
        Session session = HibernateUtil
                .getSessionFactory()
                .openSession();
        //session.beginTransaction().begin();

        if (getSchoolDay(session, DayName.MONDAY) == null) {
            SchoolDay schoolDay = new SchoolDay();
            schoolDay.setDayName(DayName.MONDAY);
            session.save(schoolDay);
            //session.beginTransaction().commit();
        }


        if (getSchoolDay(session, DayName.TUESDAY) == null) {
            // session.beginTransaction().begin();
            SchoolDay schoolDay = new SchoolDay();
            schoolDay.setDayName(DayName.TUESDAY);
            session.save(schoolDay);
            //session.beginTransaction().commit();
        }

        if (getSchoolDay(session, DayName.WEDNESDAY) == null) {
            //session.beginTransaction().begin();
            SchoolDay schoolDay = new SchoolDay();
            schoolDay.setDayName(DayName.WEDNESDAY);
            session.save(schoolDay);
            //session.beginTransaction().commit();
        }


        if (getSchoolDay(session, DayName.THURSDAY) == null) {
            //session.beginTransaction().begin();
            SchoolDay schoolDay = new SchoolDay();
            schoolDay.setDayName(DayName.THURSDAY);
            session.save(schoolDay);
            //session.beginTransaction().commit();
        }


        if (getSchoolDay(session, DayName.FRIDAY) == null) {
            // session.beginTransaction().begin();
            SchoolDay schoolDay = new SchoolDay();
            schoolDay.setDayName(DayName.FRIDAY);
            session.save(schoolDay);
        }
        session.beginTransaction().commit();
        session.close();
    }

    private SchoolDay getSchoolDay(Session session, String dayName) {
        SchoolDay sd = (SchoolDay) session.createQuery("from SchoolDay as sd where sd.dayName = :dayName")
                .setParameter("dayName", dayName)
                .uniqueResult();
        return sd;
    }

    @Override
    public void stop() throws Exception {
        HibernateUtil.shutdown();
        SchedulerUtil.shutdown();
        super.stop();
    }
}
