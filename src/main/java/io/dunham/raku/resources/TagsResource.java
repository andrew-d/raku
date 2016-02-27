package io.dunham.raku.resources;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.dropwizard.hibernate.UnitOfWork;

import io.dunham.raku.core.Tag;
import io.dunham.raku.db.DocumentDAO;
import io.dunham.raku.db.TagDAO;


@Path("/tags")
@Produces(MediaType.APPLICATION_JSON)
public class TagsResource {

    private final DocumentDAO documentDAO;
    private final TagDAO tagsDAO;

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
