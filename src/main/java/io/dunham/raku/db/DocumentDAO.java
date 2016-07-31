package io.dunham.raku.db;

import com.google.common.base.Optional;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterContainerMapper;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;
import org.skife.jdbi.v2.util.LongMapper;

import io.dunham.raku.model.Document;
import io.dunham.raku.model.Documents;
import io.dunham.raku.model.DocumentsContainerFactory;
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

    @RegisterContainerMapper(DocumentsContainerFactory.class)
    @SqlQuery("SELECT * FROM documents OFFSET :pg.offset LIMIT :pg.limit")
    Documents findAll(@BindBean("pg") PaginationParams pagination);

    @RegisterContainerMapper(DocumentsContainerFactory.class)
    @SqlQuery("SELECT d.* FROM documents d "
            + "INNER JOIN document_tags dt "
            + "ON (d.document_id = dt.document_id) "
            + "WHERE dt.tag_id = :id "
            + "LIMIT :pg.limit OFFSET :pg.offset")
    Documents findByTag(@Bind("id") long id,
                        @BindBean("pg") PaginationParams pagination);

    @SqlUpdate("INSERT INTO documents (name) VALUES (:name)")
    @GetGeneratedKeys
    long save(@BindBean Document document);

    @SqlUpdate("DELETE FROM documents WHERE document_id = :id")
    void delete(@BindBean Document document);
}
