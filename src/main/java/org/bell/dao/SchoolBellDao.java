package org.bell.dao;

import org.bell.entity.SchoolDay;
import org.bell.framework.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hkn on 22.03.2016.
 */
public class SchoolBellDao {

    public ArrayList<SchoolDay> getSchoolDays() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List list = session.createQuery("from SchoolDay as sd where sd.bellTimes.size > 0 group by sd.id")
                .list();
        session.close();
        return (ArrayList<SchoolDay>) list;
    }

    public void clearSchoolDaysBellTime(SchoolDay schoolDay) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        schoolDay.getBellTimes().forEach(session::delete);
        session.flush();
        session.getTransaction().commit();
    }

    public ArrayList<SchoolDay> getSchoolDaysNotComputed() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List list = session.createQuery("from SchoolDay where bellTimes.size = 0 group by id")
                .list();
        session.close();
        return (ArrayList<SchoolDay>) list;
    }


    public boolean checkDayForNullBell(String dayName) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        SchoolDay sd = getSchoolDay(dayName, session);
        session.close();
        return sd.getBellTimes() == null;
    }

    private SchoolDay getSchoolDay(String dayName, Session session) {
        Query query = session.createQuery("from SchoolDay as sd where sd.dayName = :dayName");
        query.setParameter("dayName", dayName);
        return (SchoolDay) query.uniqueResult();
    }

    public void save(SchoolDay schoolDay) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.merge(schoolDay);
        //session.save(schoolDay);
        session.flush();
        session.getTransaction().commit();
    }

    public SchoolDay getBellTimesByGivenDay(String dayName) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        SchoolDay sd = getSchoolDay(dayName, session);
        session.close();
        return sd;
    }
}

