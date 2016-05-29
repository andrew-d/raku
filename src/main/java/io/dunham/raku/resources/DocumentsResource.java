package io.dunham.raku.resources;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import io.dropwizard.jersey.params.LongParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dunham.raku.db.DocumentDAO;
import io.dunham.raku.db.TagDAO;
import io.dunham.raku.helpers.pagination.PaginationHelpers;
import io.dunham.raku.helpers.pagination.PaginationParams;
import io.dunham.raku.model.Document;


@Path("/documents")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class DocumentsResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentsResource.class);
    private static final LongParam DEFAULT_PAGE = new LongParam("1");
    private static final LongParam DEFAULT_PER_PAGE = new LongParam("20");

    private final DocumentDAO documentDAO;
    private final TagDAO tagDAO;

    @Inject
    public DocumentsResource(DocumentDAO docDAO, TagDAO tagDAO) {
        this.documentDAO = docDAO;
        this.tagDAO = tagDAO;
    }

    @Timed
    @POST
    public Document createDocument(@Valid Document document) {
        final long id = documentDAO.save(document);
        document.setId(id);
        return document;
    }

    @Timed
    @GET
    public List<Document> listDocuments(
        @Context PaginationParams pagination,
        @Context ContainerRequestContext ctx
    ) {
        final List<Document> documents = documentDAO.findAll(pagination);

        // Save pagination in request context so response filter can use it.
        final long count = documentDAO.count();
        pagination.setTotal(count);
        PaginationHelpers.setParams(ctx, pagination);

        return documents;
    }
}
