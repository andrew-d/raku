package io.dunham.raku.db;

import java.util.List;

import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import io.dunham.raku.core.Document;


public class DocumentDAO extends AbstractDAO<Document> {
    public DocumentDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Document> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public Document create(Document person) {
        return persist(person);
    }

    public List<Document> findAll() {
        return list(namedQuery("io.dunham.raku.core.Document.findAll"));
    }
}
