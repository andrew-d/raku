package io.dunham.raku.resources;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
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
import io.dunham.raku.model.Tag;
import io.dunham.raku.model.Tags;


@Path("/tags")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class TagsResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagsResource.class);
    private static final LongParam DEFAULT_PAGE = new LongParam("1");
    private static final LongParam DEFAULT_PER_PAGE = new LongParam("20");

    private final DocumentDAO documentDAO;
    private final TagDAO tagDAO;

    @Inject
    public TagsResource(DocumentDAO docDAO, TagDAO tagDAO) {
        this.documentDAO = docDAO;
        this.tagDAO = tagDAO;
    }

    @Timed
    @POST
    public Tag createTag(@Valid Tag tag) {
        final long id = tagDAO.save(tag);
        tag.setId(id);
        return tag;
    }

    @Timed
    @GET
    public Tags listTags(
        @Context PaginationParams pagination,
        @Context ContainerRequestContext ctx
    ) {
        final Tags tags = tagDAO.findAll(pagination);

        // Save pagination in request context so response filter can use it.
        final long count = tagDAO.count();
        pagination.setTotal(count);
        PaginationHelpers.setParams(ctx, pagination);

        return tags;
    }

    private long ensurePositive(long input) {
        if (input < 0) {
            throw new BadRequestException("Value must be positive");
        }
        return input;
    }
}
