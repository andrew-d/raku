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
import java.util.stream.Collectors;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import io.dropwizard.jersey.params.LongParam;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dunham.raku.model.Document;
import io.dunham.raku.db.DocumentDAO;
import io.dunham.raku.db.TagDAO;
import io.dunham.raku.dto.DocumentWithTagsDTO;


@Path("/documents/{documentId}")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class DocumentResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentResource.class);

    private final DocumentDAO documentDAO;
    private final TagDAO tagDAO;

    @Inject
    public DocumentResource(DocumentDAO docDAO, TagDAO tagDAO) {
        this.documentDAO = docDAO;
        this.tagDAO = tagDAO;
    }

    @GET
    @UnitOfWork
    public DocumentWithTagsDTO getDocument(@PathParam("documentId") LongParam documentId) {
        return new DocumentWithTagsDTO(findSafely(documentId.get()));
    }

    private Document findSafely(long documentId) {
        final Optional<Document> document = documentDAO.findById(documentId);
        if (!document.isPresent()) {
            throw new NotFoundException("No such document");
        }
        return document.get();
    }
}

