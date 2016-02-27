package io.dunham.raku.db;

import java.util.List;

import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import io.dunham.raku.core.Tag;


public class TagDAO extends AbstractDAO<Tag> {
    public TagDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Tag> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public Tag create(Tag person) {
        return persist(person);
    }

    public List<Tag> findAll() {
        return list(namedQuery("io.dunham.raku.core.Tag.findAll"));
    }
}
