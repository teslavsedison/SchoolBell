package org.bell.app;

import com.sun.javafx.application.LauncherImpl;
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
import org.hibernate.criterion.Restrictions;

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
            LauncherImpl.launchApplication(Main.class, SchoolBellPreloader.class, args);
            //launch(args);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        SchedulerUtil.configure();
        setDb();
        Localization.setLocale(new Locale("tr-TR"));
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/MainView.fxml"));
        primaryStage.setTitle("Okul Zili");
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("bell1.png")));
        Scene scene = new Scene(root, 400, 520);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setDb() {
        Session session = HibernateUtil
                .getSessionFactory()
                .openSession();

        session.beginTransaction().begin();
        Object result = null;
        result = session.createCriteria(SchoolDay.class)
                .add(Restrictions.eq("dayName", DayName.MONDAY))
                .uniqueResult();
        if (result == null) {
            SchoolDay schoolDay = new SchoolDay();
            schoolDay.setDayName(DayName.MONDAY);
            session.save(schoolDay);
        }
        session.beginTransaction().commit();

        session.beginTransaction().begin();
        result = session.createCriteria(SchoolDay.class)
                .add(Restrictions.eq("dayName", DayName.TUESDAY))
                .uniqueResult();
        if (result == null) {
            SchoolDay schoolDay = new SchoolDay();
            schoolDay.setDayName(DayName.TUESDAY);
            session.save(schoolDay);
        }
        session.beginTransaction().commit();

        session.beginTransaction().begin();
        result = session.createCriteria(SchoolDay.class)
                .add(Restrictions.eq("dayName", DayName.WEDNESDAY))
                .uniqueResult();
        if (result == null) {
            SchoolDay schoolDay = new SchoolDay();
            schoolDay.setDayName(DayName.WEDNESDAY);
            session.save(schoolDay);
        }
        session.beginTransaction().commit();

        session.beginTransaction().begin();
        result = session.createCriteria(SchoolDay.class)
                .add(Restrictions.eq("dayName", DayName.THURSDAY))
                .uniqueResult();
        if (result == null) {
            SchoolDay schoolDay = new SchoolDay();
            schoolDay.setDayName(DayName.THURSDAY);
            session.save(schoolDay);
        }
        session.beginTransaction().commit();

        session.beginTransaction().begin();
        result = session.createCriteria(SchoolDay.class)
                .add(Restrictions.eq("dayName", DayName.FRIDAY))
                .uniqueResult();
        if (result == null) {
            SchoolDay schoolDay = new SchoolDay();
            schoolDay.setDayName(DayName.FRIDAY);
            session.save(schoolDay);
        }
        session.beginTransaction().commit();
        session.close();
    }

    @Override
    public void stop() throws Exception {
        HibernateUtil.shutdown();
        SchedulerUtil.shutdown();
        super.stop();
    }
}


//import org.hibernate.HibernateException;
//import org.hibernate.SessionFactory;
//import org.hibernate.Session;
//import org.hibernate.Query;
//import org.hibernate.cfg.Configuration;
//import org.hibernate.metadata.ClassMetadata;
//import org.hibernate.service.ServiceRegistry;
//import org.hibernate.service.ServiceRegistryBuilder;
//
//import java.util.Map;
//
///**
// * Created by hkn on 16.03.2016.
// */
//public class Main {
//    private static final SessionFactory ourSessionFactory;
//    private static final ServiceRegistry serviceRegistry;
//
//    static {
//        try {
//            Configuration configuration = new Configuration();
//            configuration.configure();
//
//            serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
//            ourSessionFactory = configuration.buildSessionFactory(serviceRegistry);
//        } catch (Throwable ex) {
//            throw new ExceptionInInitializerError(ex);
//        }
//    }
//
//    public static Session getSession() throws HibernateException {
//        return ourSessionFactory.openSession();
//    }
//
//    public static void main(final String[] args) throws Exception {
//        final Session session = getSession();
//        try {
//            System.out.println("querying all the managed entities...");
//            final Map metadataMap = session.getSessionFactory().getAllClassMetadata();
//            for (Object key : metadataMap.keySet()) {
//                final ClassMetadata classMetadata = (ClassMetadata) metadataMap.get(key);
//                final String entityName = classMetadata.getEntityName();
//                final Query query = session.createQuery("from " + entityName);
//                System.out.println("executing: " + query.getQueryString());
//                for (Object o : query.list()) {
//                    System.out.println("  " + o);
//                }
//            }
//        } finally {
//            session.close();
//        }
//    }
//}

