package io.dunham.raku.db;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.base.Optional;
import org.hibernate.SessionFactory;

import io.dunham.raku.model.Document;
import io.dunham.raku.model.File;
import io.dunham.raku.model.QFile;


@Singleton
public class FileDAO extends GenericDAO<File> {
    private QFile file = QFile.file;

    @Inject
    public FileDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<File> findByHash(String hash) {
        File f = query().selectFrom(file)
            .where(file.hash.eq(hash))
            .fetchOne();
        return Optional.fromNullable(f);
    }

    public List<File> findByDocument(Document doc) {
        return query().selectFrom(file)
            .where(file.document.eq(doc))
            .fetch();
    }

    public Optional<File> findByDocumentAndHash(Document doc, String hash) {
        File f = query().selectFrom(file)
            .where(file.hash.eq(hash), file.document.eq(doc))
            .fetchOne();
        return Optional.fromNullable(f);
    }

    public List<File> findAll() {
        return query().selectFrom(file).fetch();
    }
}
