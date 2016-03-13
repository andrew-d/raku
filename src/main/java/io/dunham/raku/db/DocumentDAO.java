package io.dunham.raku.db;

import java.util.List;

import com.google.common.base.Optional;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

import io.dunham.raku.model.Document;
import io.dunham.raku.model.Tag;


@RegisterMapper(DocumentMapper.class)
public interface DocumentDAO {
    @SqlQuery("SELECT * FROM documents WHERE document_id = :it")
    @SingleValueResult
    Optional<Document> findById(@Bind long id);

    @SqlQuery("SELECT * FROM documents OFFSET :offset LIMIT :limit")
    List<Document> findAll(@Bind("offset") long offset,
                           @Bind("limit") long limit);

    @SqlQuery("SELECT d.* FROM documents d "
            + "INNER JOIN document_tags dt "
            + "ON (d.document_id = dt.document_id) "
            + "WHERE dt.tag_id = :t.tag_id "
            + "LIMIT :limit OFFSET :offset")
    List<Document> findByTag(@BindBean("t") Tag tag,
                             @Bind("offset") long offset,
                             @Bind("limit") long limit);

    @SqlUpdate("INSERT INTO documents (name) VALUES (:name)")
    @GetGeneratedKeys
    long save(@BindBean Document document);
}
