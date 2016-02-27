package io.dunham.raku.util;

import java.util.concurrent.Callable;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;


public class HibernateRunner {
    private SessionFactory sessionFactory;

    public HibernateRunner(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    public <T> T withHibernate(Callable<T> c) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            ManagedSessionContext.bind(session);
            Transaction tr = session.beginTransaction();

            try {
                T ret = c.call();
                tr.commit();

                return ret;
            } catch (Exception e) {
                tr.rollback();
                throw e;
            }
        } finally {
            session.close();
            ManagedSessionContext.unbind(sessionFactory);
        }
    }
}
