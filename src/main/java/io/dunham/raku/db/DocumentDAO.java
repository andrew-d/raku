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
import org.skife.jdbi.v2.util.LongMapper;

import io.dunham.raku.model.Document;
import io.dunham.raku.model.Tag;
import io.dunham.raku.helpers.pagination.PaginationParams;


@RegisterMapper(DocumentMapper.class)
public interface DocumentDAO {
    @SqlQuery("SELECT * FROM documents WHERE document_id = :it")
    @SingleValueResult
    Optional<Document> findById(@Bind long id);

    @SqlQuery("SELECT COUNT(*) FROM documents")
    @RegisterMapper(LongMapper.class)
    Long count();

    @SqlQuery("SELECT * FROM documents OFFSET :pg.offset LIMIT :pg.limit")
    List<Document> findAll(@BindBean("pg") PaginationParams pagination);

    @SqlQuery("SELECT d.* FROM documents d "
            + "INNER JOIN document_tags dt "
            + "ON (d.document_id = dt.document_id) "
            + "WHERE dt.tag_id = :id "
            + "LIMIT :pg.limit OFFSET :pg.offset")
    List<Document> findByTag(@Bind("id") long id,
                             @BindBean("pg") PaginationParams pagination);

    @SqlUpdate("INSERT INTO documents (name) VALUES (:name)")
    @GetGeneratedKeys
    long save(@BindBean Document document);
}
