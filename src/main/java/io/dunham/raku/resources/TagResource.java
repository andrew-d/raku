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
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PathParam;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import io.dropwizard.jersey.params.LongParam;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dunham.raku.model.Tag;
import io.dunham.raku.db.DocumentDAO;
import io.dunham.raku.db.TagDAO;
import io.dunham.raku.dto.TagWithDocumentsDTO;


@Path("/tags/{tagId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class TagResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagResource.class);

    private final DocumentDAO documentDAO;
    private final TagDAO tagDAO;

    @Inject
    public TagResource(DocumentDAO docDAO, TagDAO tagDAO) {
        this.documentDAO = docDAO;
        this.tagDAO = tagDAO;
    }

    @GET
    @UnitOfWork
    public TagWithDocumentsDTO getTag(@PathParam("tagId") LongParam tagId) {
        return new TagWithDocumentsDTO(findSafely(tagId.get()));
    }

    private Tag findSafely(long tagId) {
        final Optional<Tag> tag = tagDAO.findById(tagId);
        if (!tag.isPresent()) {
            throw new NotFoundException("No such tag");
        }
        return tag.get();
    }
}
