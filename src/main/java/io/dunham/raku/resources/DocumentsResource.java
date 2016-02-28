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

import io.dunham.raku.model.Document;
import io.dunham.raku.db.DocumentDAO;
import io.dunham.raku.db.TagDAO;
import io.dunham.raku.dto.DocumentDTO;
import io.dunham.raku.dto.DocumentWithTagsDTO;


@Path("/documents")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class DocumentsResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentsResource.class);

    private final DocumentDAO documentDAO;
    private final TagDAO tagDAO;

    @Inject
    public DocumentsResource(DocumentDAO docDAO, TagDAO tagDAO) {
        this.documentDAO = docDAO;
        this.tagDAO = tagDAO;
    }

    @POST
    @UnitOfWork
    public DocumentWithTagsDTO createDocument(Document document) {
        return new DocumentWithTagsDTO(documentDAO.create(document));
    }

    @GET
    @UnitOfWork
    public List<DocumentDTO> listDocuments() {
        return DocumentDTO.mapList(documentDAO.findAll());
    }
}
