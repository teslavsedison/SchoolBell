package org.bell.dao;

import org.bell.entity.BellTime;
import org.bell.entity.SchoolDay;
import org.bell.framework.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hkn on 22.03.2016.
 */
public class SchoolBellDao {

    public ArrayList<SchoolDay> getSchoolDays() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List list = session.createCriteria(SchoolDay.class)
                .list();
        session.close();
        return (ArrayList<SchoolDay>) list;
    }


    public boolean checkDayForNullBell(String dayName) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        SchoolDay sd = (SchoolDay) session.createCriteria(SchoolDay.class)
                .add(Restrictions.eq("dayName", dayName))
                .uniqueResult();
        session.close();
        return sd.getBellTimes() == null;
    }

    public SchoolDay createBellTimes(String dayName, List<BellTime> bellTimes) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        SchoolDay sd = (SchoolDay) session.createCriteria(SchoolDay.class)
                .add(Restrictions.eq("dayName", dayName))
                .uniqueResult();
        sd.getBellTimes().clear();
        sd.getBellTimes().addAll(bellTimes);
        session.save(sd);
        transaction.commit();
        return (SchoolDay) session.createCriteria(SchoolDay.class)
                .add(Restrictions.eq("dayName", bellTimes))
                .uniqueResult();
    }
}

