package io.dunham.raku.resources;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dunham.raku.db.DocumentDAO;
import io.dunham.raku.db.TagDAO;
import io.dunham.raku.model.Tag;
import io.dunham.raku.viewmodel.TagWithDocumentIdsVM;
import io.dunham.raku.viewmodel.TagWithEmbeddedDocumentsVM;


@Path("/tags")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class TagsResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagsResource.class);

    private final DocumentDAO documentDAO;
    private final TagDAO tagDAO;

    @Inject
    public TagsResource(DocumentDAO docDAO, TagDAO tagDAO) {
        this.documentDAO = docDAO;
        this.tagDAO = tagDAO;
    }

    @POST
    @UnitOfWork
    public TagWithEmbeddedDocumentsVM createTag(Tag tag) {
        return new TagWithEmbeddedDocumentsVM(tagDAO.saveOrUpdate(tag));
    }

    @GET
    @UnitOfWork
    public List<TagWithDocumentIdsVM> listTags() {
        return TagWithDocumentIdsVM.mapList(tagDAO.findAll());
    }
}
