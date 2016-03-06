package io.dunham.raku.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteStreams;
import static com.google.common.base.Preconditions.checkNotNull;


public class CAStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(CAStore.class);

    private final Path rootPath;
    private final boolean saveExtension;

    public CAStore(Path rootPath, boolean saveExtension) {
        this.rootPath = checkNotNull(rootPath);
        this.saveExtension = checkNotNull(saveExtension);

        // Create the root path.
        try {
            Files.createDirectories(rootPath);
        } catch (final FileAlreadyExistsException e) {
            // If it already exists, no problem.
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CAStore(Path rootPath) {
        this(rootPath, false);
    }

    // --------------------------------------------------

    public Info save(InputStream aFile, String aExtension) throws IOException {
        final InputStream file = checkNotNull(aFile);
        final String extension = checkNotNull(aExtension);

        // Create a temporary file.
        final File tempFile = File.createTempFile("castore-tmp", ".tmp");

        // Create an input stream that also hashes.
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        // Copy from one to the other.
        try (final DigestInputStream in = new DigestInputStream(file, md);
                final FileOutputStream out = new FileOutputStream(tempFile)) {
            ByteStreams.copy(in, out);
        }

        // If we get here, we've copied successfully.  Get the hash.
        byte digest[] = md.digest();
        final String hash = BaseEncoding.base16().encode(digest).toUpperCase();

        // If the file already exists, then we just return the hash and delete
        // our temporary file.
        if (exists(hash, extension)) {
            try {
                Files.delete(tempFile.toPath());
            } catch (final IOException e) {}

            return infoFromPath(hash, extension);
        }

        // Build the output path.
        final Path saveDir = getSaveDir(hash);
        final Path saveFile = getSaveFile(hash, extension);
        LOGGER.debug("Saving file as: {}", saveFile);

        // Create the directory.
        createDirectoryIfNotExists(saveDir);

        // Atomically move the file into place, if possible.
        tryAtomicMove(tempFile.toPath(), saveFile);

        // Fetch info about the file, now that everything's a success.
        return infoFromPath(hash, extension);
    }

    public Info save(InputStream file) throws IOException {
        return save(file, "");
    }

    /**
     * Returns whether the given file exists in the store.
     *
     * @param hash      hash of the file
     * @param extension extension of the file
     * @return          {@code true} if the file exists in the store, otherwise {@code false}
     */
    public boolean exists(String hash, String extension) throws IOException {
        final Path filePath = getSaveFile(checkNotNull(hash), checkNotNull(extension));
        return Files.isRegularFile(filePath) && Files.isReadable(filePath);
    }

    /**
     * Returns whether the given file exists in the store.  This method works
     * exactly like callling {@code exists(hash, "")}.
     *
     * @param hash      hash of the file
     * @return          {@code true} if the file exists in the store, otherwise {@code false}
     */
    public boolean exists(String hash) throws IOException {
        return exists(hash, "");
    }

    /**
     * Removes the given file from the store.
     *
     * @param hash      hash of the file to remove
     * @param extension extension of the file to remove
     * @return          {@code true} if the file existed and was deleted, otherwise {@code false}
     */
    public boolean remove(String hash, String extension) throws IOException {
        final Path filePath = getSaveFile(checkNotNull(hash), checkNotNull(extension));
        return Files.deleteIfExists(filePath);
    }

    /**
     * Removes the given file from the store.  This method works exactly like
     * callling {@code remove(hash, "")}.
     *
     * @param hash      hash of the file to remove
     * @return          {@code true} if the file existed and was deleted, otherwise {@code false}
     */
    public boolean remove(String hash) throws IOException {
        return remove(hash, "");
    }

    // --------------------------------------------------

    private Info infoFromPath(String hash, String extension) throws IOException {
        final Path path = getSaveFile(hash, extension);
        final long size = Files.size(path);
        final String contentType = Files.probeContentType(path);

        return new Info(hash, size, contentType);
    }

    private Path getSaveDir(String hash) {
        return rootPath.resolve(hash.substring(0, 2));
    }

    private Path getSaveFile(String hash, String extension) {
        final StringBuilder filename = new StringBuilder(hash);
        if (saveExtension) {
            filename.append(".");
            filename.append(extension);
        }

        return getSaveDir(hash).resolve(filename.toString());
    }

    private void createDirectoryIfNotExists(Path path) throws IOException {
        try {
            Files.createDirectory(path);
        } catch (final FileAlreadyExistsException e) {
        }
    }

    private void tryAtomicMove(Path from, Path to) throws IOException {
        try {
            Files.move(from, to, ATOMIC_MOVE);
        } catch (final AtomicMoveNotSupportedException e) {
            LOGGER.warn("ATOMIC_MOVE not supported - moving normally");

            Files.move(from, to);
        }
    }

    // --------------------------------------------------

    public static class Info {
        public String hash;
        public long size;
        public String contentType;

        public Info(String hash, long size, String contentType) {
            this.hash = hash;
            this.size = size;
            this.contentType = contentType;
        }
    }
}
