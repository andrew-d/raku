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

import io.dunham.raku.helpers.pagination.PaginationParams;
import io.dunham.raku.model.Document;
import io.dunham.raku.model.Tag;


@RegisterMapper(TagMapper.class)
public interface TagDAO {
    @SqlQuery("SELECT * FROM tags WHERE tag_id = :it")
    @SingleValueResult
    Optional<Tag> findById(@Bind long id);

    @SqlQuery("SELECT * FROM tags WHERE name = :it")
    @SingleValueResult
    Optional<Tag> findByName(@Bind String name);

    @SqlQuery("SELECT COUNT(*) FROM tags")
    @RegisterMapper(LongMapper.class)
    Long count();

    @SqlQuery("SELECT * FROM tags OFFSET :pg.offset LIMIT :pg.limit")
    List<Tag> findAll(@BindBean("pg") PaginationParams pagination);

    @SqlQuery("SELECT t.* FROM tags t "
            + "INNER JOIN document_tags dt "
            + "ON (t.tag_id = dt.tag_id) "
            + "WHERE dt.document_id = :id")
    List<Tag> findAllByDocument(@BindBean Document document);

    @SqlQuery("SELECT t.* FROM tags t "
            + "INNER JOIN document_tags dt "
            + "ON (t.tag_id = dt.tag_id) "
            + "WHERE dt.document_id = :id "
            + "LIMIT :pg.limit OFFSET :pg.offset")
    List<Tag> findByDocument(@Bind("id") long id,
                             @BindBean("pg") PaginationParams pagination);

    @SqlUpdate("INSERT INTO tags (name) VALUES (:name)")
    @GetGeneratedKeys
    long save(@BindBean Tag tag);

    @SqlUpdate("INSERT INTO document_tags (document_id, tag_id) "
             + "VALUES (:d.id, :t.id)")
    void addToDocument(@BindBean("d") Document document, @BindBean("t") Tag t);
}
