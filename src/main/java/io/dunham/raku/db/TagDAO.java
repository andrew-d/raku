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


@RegisterMapper(TagMapper.class)
public interface TagDAO {
    @SqlQuery("SELECT * FROM tags WHERE tag_id = :it")
    @SingleValueResult
    Optional<Tag> findById(@Bind long id);

    @SqlQuery("SELECT * FROM tags OFFSET :offset LIMIT :limit")
    List<Tag> findAll(@Bind("offset") long offset,
                      @Bind("limit") long limit);

    @SqlQuery("SELECT t.* FROM tags t "
            + "INNER JOIN document_tags dt "
            + "ON (t.tag_id = dt.tag_id) "
            + "WHERE dt.document_id = :d.document_id "
            + "LIMIT :limit OFFSET :offset")
    List<Tag> findByDocument(@BindBean("d") Document doc,
                             @Bind("offset") long offset,
                             @Bind("limit") long limit);

    @SqlUpdate("INSERT INTO tags (name) VALUES (:name)")
    @GetGeneratedKeys
    long save(@BindBean Tag tag);
}
