package io.dunham.raku.db;

import java.util.Collection;

import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;


public abstract class GenericDAO<T> extends AbstractDAO<T> {
    private HibernateQueryFactory factory;

    protected GenericDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.factory = new HibernateQueryFactory(() -> currentSession());
    }

    protected HibernateQueryFactory query() {
        return factory;
    }

    public void saveOrUpdate(T model) {
        persist(model);
    }

    public void saveOrUpdate(Collection<T> models) {
        models.forEach(m -> persist(m));
    }

    public T findById(Long id) {
        return get(id);
    }

    public void delete(T object) {
        if (object != null) {
            currentSession().delete(object);
        }
    }

    public int delete(Collection<T> objects) {
        objects.forEach(o -> delete(o));
        return objects.size();
    }
}
