package io.dunham.raku.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.io.Files;
import io.dropwizard.jersey.params.LongParam;
import io.dropwizard.jersey.params.NonEmptyStringParam;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.dunham.raku.db.DocumentDAO;
import io.dunham.raku.db.FileDAO;
import io.dunham.raku.db.TagDAO;
import io.dunham.raku.filtering.DocumentWithTagsView;
import io.dunham.raku.model.Document;
import io.dunham.raku.model.Tag;
import io.dunham.raku.model.Tags;
import io.dunham.raku.model.File;
import io.dunham.raku.util.CAStore;


@Path("/documents/{documentId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class DocumentResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentResource.class);

    private final DocumentDAO documentDAO;
    private final FileDAO fileDAO;
    private final TagDAO tagDAO;
    private final CAStore store;
    private final Tika tika;

    @Inject
    public DocumentResource(DocumentDAO docDAO, FileDAO fileDAO, TagDAO tagDAO, CAStore store) {
        this.documentDAO = docDAO;
        this.fileDAO = fileDAO;
        this.tagDAO = tagDAO;
        this.store = store;
        this.tika = new Tika();
    }

    @Timed
    @GET
    @DocumentWithTagsView
    public Document getDocument(@PathParam("documentId") LongParam documentId) {
        final Document doc = findSafely(documentId.get());
        doc.setTags(tagDAO.findAllByDocument(doc));
        return doc;
    }

    @Timed
    @DELETE
    public Response deleteDocument(@PathParam("documentId") LongParam documentId) {
        final Document d = findSafely(documentId.get());
        documentDAO.delete(d);
        return Response.status(204).build();
    }

    @Timed
    @POST
    @Path("/tags")
    public Tags addTag(@PathParam("documentId") LongParam documentId,
                       @Valid Tag inputTag) {
        final Document d = findSafely(documentId.get());

        // Get or create the tag.
        final Tag tag = tagDAO.findByName(inputTag.getName()).or(() -> {
            LOGGER.debug("Creating new tag with name: {}", inputTag.getName());

            final long insertedId = tagDAO.save(inputTag);
            inputTag.setId(insertedId);
            return inputTag;
        });

        // Add it to this document.
        tagDAO.addToDocument(d, tag);

        // Return all tags on this document.
        return tagDAO.findAllByDocument(d);
    }

    @Timed
    @PUT
    @Path("/tags")
    public Tags setTags(@PathParam("documentId") LongParam documentId,
                        @Valid Tags inputTags) {
        final Document doc = findSafely(documentId.get());

        // Get or create all the tags.
        // TODO: Use `MERGE` to upsert, removing any race condition.
        final Tags tags = inputTags.stream()
          .map((tag) -> {
              return tagDAO.findByName(tag.getName()).or(() -> {
                  LOGGER.debug("Creating new tag with name: {}", tag.getName());

                  final long insertedId = tagDAO.save(tag);
                  tag.setId(insertedId);
                  return tag;
              });
          })
          .collect(Collectors.toCollection(Tags::new));

        // As a transaction, reset this document's tags.
        tagDAO.inTransaction((tx, status) -> {
            tx.deleteByDocument(doc.getId());

            for (final Tag tag : tags) {
              tx.addToDocument(doc, tag);
            }

            return 0;
        });

        return tags;
    }

    @Timed
    @GET
    @Path("/files")
    public List<File> getFiles(@PathParam("documentId") LongParam documentId) {
        final Document doc = findSafely(documentId.get());
        final List<File> files = fileDAO.findByDocument(doc);
        return files;
    }

    @Timed
    @GET
    @Path("/files/{hash}")
    public Response getFile(@PathParam("documentId") LongParam documentId,
                            @PathParam("hash") NonEmptyStringParam hashParam) {
        if (!hashParam.get().isPresent()) {
            throw new BadRequestException("No hash given");
        }

        final String hash = hashParam.get().get();
        LOGGER.info("Getting document {}, file {}", documentId.get(), hash);

        final Document doc = findSafely(documentId.get());
        final Optional<File> f = fileDAO.findByDocumentAndHash(doc, hash);

        if (!f.isPresent()) {
            throw new NotFoundException("No such file");
        }

        return Response.ok().entity(f.get()).build();
    }

    @Timed
    @DELETE
    @Path("/files/{hash}")
    public Response deleteFile(@PathParam("documentId") LongParam documentId,
                               @PathParam("hash") NonEmptyStringParam hashParam) {
        if (!hashParam.get().isPresent()) {
            throw new BadRequestException("No hash given");
        }

        final String hash = hashParam.get().get();
        LOGGER.info("Getting document {}, file {}", documentId.get(), hash);

        final Document doc = findSafely(documentId.get());
        final Optional<File> f = fileDAO.findByDocumentAndHash(doc, hash);
        if (!f.isPresent()) {
            throw new NotFoundException("No such file");
        }

        fileDAO.delete(f.get());
        return Response.status(204).build();
    }

    @Timed
    @GET
    @Path("/files/{hash}/content")
    public Response getFileContent(@PathParam("documentId") LongParam documentId,
                                   @PathParam("hash") NonEmptyStringParam hashParam) {
        if (!hashParam.get().isPresent()) {
            throw new BadRequestException("No hash given");
        }

        final String hash = hashParam.get().get();
        LOGGER.info("Getting document {}, file {}", documentId.get(), hash);

        final Document doc = findSafely(documentId.get());
        final Optional<File> of = fileDAO.findByDocumentAndHash(doc, hash);

        if (!of.isPresent()) {
            throw new NotFoundException("No such file");
        }

        final File f = of.get();

        String contentType = "application/octet-stream";
        if (f.getContentType() != null) {
            contentType = f.getContentType();
        }

        try {
            final InputStream fis = store.open(f.getHash(), f.fileExtension());
            return Response.ok(fis, contentType).build();

        } catch (final IOException e) {
            LOGGER.error("Exception opening file: {}", e);
            return Response.status(500).build();
        }
    }

    @Timed
    @POST
    @Path("/files")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response addFile(@PathParam("documentId") LongParam documentId,
                            @FormDataParam("file") final InputStream fileInputStream,
                            @FormDataParam("file") final FormDataContentDisposition fileDetail) {
        // Get the corresponding document
        final Document doc = findSafely(documentId.get());
        String fileName = fileDetail.getFileName();
        String extension = Files.getFileExtension(fileDetail.getFileName());

        if (fileName == null) {
            fileName = "";
        }
        if (extension == null) {
            extension = "";
        }

        LOGGER.info("Uploading file named '{}' to document {}", fileName, documentId);

        // Save the file.
        CAStore.Info info;
        try {
            info = store.save(fileInputStream, extension);
            LOGGER.debug("Uploaded file info for '{}': {}", fileName, info);
        } catch (final IOException e) {
            LOGGER.error("Exception saving file: {}", e);
            return Response.status(500).build();
        }

        // Re-open the file on disk.
        String fileContents = null;
        try (final InputStream fis = store.open(info.hash, extension)) {
            fileContents = this.tika.parseToString(fis);

        } catch (final TikaException e) {
            LOGGER.warn("Could not extract text from file '{}': {}", fileName, e);

        } catch (final IOException e) {
            LOGGER.error("Exception re-opening file: {}", e);
            return Response.status(500).build();
        }

        if (fileContents != null) {
            LOGGER.debug("Extracted {} bytes of text from file '{}'", fileContents.length(), fileName);
        }

        // Create a new file
        final File newFile = new File(info.hash, info.size, fileName);
        newFile.setContentType(info.contentType);

        // Remove the file if we can't save, to prevent disk clutter.
        // TODO: Do we actually want to do this?  This clobbers an existing one...
        try {
            final long id = fileDAO.save(doc, newFile);
            newFile.setId(id);
        } catch (final Exception e) {
            // Ignore errors when removing...
            try {
                store.remove(info.hash);
            } catch (final IOException ign) {}

            // ... since we just re-throw the original error.
            throw e;
        }

        // All good
        return Response.ok().entity(newFile).build();
    }

    private Document findSafely(long documentId) {
        final Optional<Document> document = documentDAO.findById(documentId);
        if (!document.isPresent()) {
            throw new NotFoundException("No such document");
        }
        return document.get();
    }
}
