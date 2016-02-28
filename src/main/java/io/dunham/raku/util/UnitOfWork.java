package io.dunham.raku.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;


/**
 * Allows running code that needs to access the database in a lambda that
 * mimics the 'UnitOfWork' annotation that is provided by Hibernate.
 */
public class UnitOfWork {

    @FunctionalInterface
    public static interface SessionRunner {
        public void runInSession();
    }

    @FunctionalInterface
    public static interface SessionRunnerReturningValue<T> {
        public T runInSession();
    }

    public static void run(SessionFactory sessionFactory, SessionRunner runner) {
        call(sessionFactory, () -> {
            runner.runInSession();
            return null;
        });
    }

    public static <T> T call(SessionFactory sessionFactory, SessionRunnerReturningValue<T> runner) {
        final Session session = sessionFactory.openSession();

        if (ManagedSessionContext.hasBind(sessionFactory)) {
            throw new IllegalStateException("Already in a unit of work!");
        }

        T t = null;
        try {
            ManagedSessionContext.bind(session);
            session.beginTransaction();

            try {
                t = runner.runInSession();
                commitTransaction(session);
            } catch (Exception e) {
                rollbackTransaction(session);
                throw new RuntimeException(e);
            }
        } finally {
            session.close();
            ManagedSessionContext.unbind(sessionFactory);
        }

        return t;
    }

	private static void rollbackTransaction(Session session) {
		final Transaction txn = session.getTransaction();
		if (txn != null && txn.isActive()) {
			txn.rollback();
		}
	}

	private static void commitTransaction(Session session) {
		final Transaction txn = session.getTransaction();
		if (txn != null && txn.isActive()) {
			txn.commit();
		}
	}
}
