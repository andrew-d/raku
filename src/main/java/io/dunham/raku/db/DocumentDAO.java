package io.dunham.raku.db;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.base.Optional;
import org.hibernate.SessionFactory;

import io.dunham.raku.model.Document;
import io.dunham.raku.model.QDocument;


@Singleton
public class DocumentDAO extends GenericDAO<Document> {
    private QDocument document = QDocument.document;

    @Inject
    public DocumentDAO(SessionFactory factory) {
        super(factory);
    }

    public List<Document> findAll() {
        return query().selectFrom(document).fetch();
    }
}
