package io.dunham.raku.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PathParam;

import com.google.common.base.Optional;
import io.dropwizard.jersey.params.LongParam;
import io.dropwizard.hibernate.UnitOfWork;

import io.dunham.raku.core.Tag;
import io.dunham.raku.db.DocumentDAO;
import io.dunham.raku.db.TagDAO;


@Path("/tags/{tagId}")
@Produces(MediaType.APPLICATION_JSON)
public class TagResource {

    private final DocumentDAO documentDAO;
    private final TagDAO tagsDAO;

    public TagResource(DocumentDAO docDAO, TagDAO tagsDAO) {
        this.documentDAO = docDAO;
        this.tagsDAO = tagsDAO;
    }

    @GET
    @UnitOfWork
    public Tag getTag(@PathParam("tagId") LongParam tagId) {
        return findSafely(tagId.get());
    }

    private Tag findSafely(long tagId) {
        final Optional<Tag> tag = tagsDAO.findById(tagId);
        if (!tag.isPresent()) {
            throw new NotFoundException("No such tag");
        }
        return tag.get();
    }
}
