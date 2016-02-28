package io.dunham.raku.resources;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dunham.raku.model.Tag;
import io.dunham.raku.db.DocumentDAO;
import io.dunham.raku.db.TagDAO;


@Path("/tags/{tagId}")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class TagResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagResource.class);

    private final DocumentDAO documentDAO;
    private final TagDAO tagsDAO;

    @Inject
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
