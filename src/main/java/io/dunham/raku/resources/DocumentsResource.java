package io.dunham.raku.resources;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Optional;
import io.dropwizard.jersey.params.LongParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dunham.raku.db.DocumentDAO;
import io.dunham.raku.db.TagDAO;
import io.dunham.raku.model.Document;
import io.dunham.raku.viewmodel.DocumentVM;


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

    @POST
    public DocumentVM createDocument(Document document) {
        documentDAO.save(document);
        return DocumentVM.of(document);
    }

    @GET
    public List<DocumentVM> listDocuments(
        @QueryParam("page") Optional<LongParam> pageParam,
        @QueryParam("per_page") Optional<LongParam> perPageParam
    ) {
        final long page = pageParam.or(DEFAULT_PAGE).get();
        final long perPage = pageParam.or(DEFAULT_PER_PAGE).get();

        final long offset = ensurePositive((page - 1) * perPage);
        return DocumentVM.mapList(documentDAO.findAll(offset, perPage));
    }

    private long ensurePositive(long input) {
        if (input < 0) {
            throw new BadRequestException("Value must be positive");
        }
        return input;
    }
}
