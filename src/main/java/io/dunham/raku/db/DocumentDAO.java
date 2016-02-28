package io.dunham.raku.db;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import io.dunham.raku.model.Document;


@Singleton
public class DocumentDAO extends AbstractDAO<Document> {
    @Inject
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
        return list(namedQuery("io.dunham.raku.model.Document.findAll"));
    }
}
