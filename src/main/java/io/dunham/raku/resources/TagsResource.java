package io.dunham.raku.resources;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dunham.raku.core.Tag;
import io.dunham.raku.db.DocumentDAO;
import io.dunham.raku.db.TagDAO;


@Path("/tags")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class TagsResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagsResource.class);

    private final DocumentDAO documentDAO;
    private final TagDAO tagsDAO;

    @Inject
    public TagsResource(DocumentDAO docDAO, TagDAO tagsDAO) {
        this.documentDAO = docDAO;
        this.tagsDAO = tagsDAO;
    }

    @POST
    @UnitOfWork
    public Tag createTag(Tag tag) {
        return tagsDAO.create(tag);
    }

    @GET
    @UnitOfWork
    public List<Tag> listTags() {
        return tagsDAO.findAll();
    }
}
