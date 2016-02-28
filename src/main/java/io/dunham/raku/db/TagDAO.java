package io.dunham.raku.db;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.hibernate.SessionFactory;

import io.dunham.raku.model.QTag;
import io.dunham.raku.model.Tag;


@Singleton
public class TagDAO extends GenericDAO<Tag> {
    private QTag tag = QTag.tag;

    @Inject
    public TagDAO(SessionFactory factory) {
        super(factory);
    }

    public List<Tag> findAll() {
        return query().selectFrom(tag).fetch();
    }
}
