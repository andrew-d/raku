package io.dunham.raku.db;

import java.util.Collection;

import com.google.common.base.Optional;
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

    public T saveOrUpdate(T model) {
        return persist(model);
    }

    public Collection<T> saveOrUpdate(Collection<T> models) {
        models.forEach(m -> persist(m));
        return models;
    }

    public Optional<T> findById(Long id) {
        return Optional.fromNullable(get(id));
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
