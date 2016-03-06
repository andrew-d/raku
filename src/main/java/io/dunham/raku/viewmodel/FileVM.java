package io.dunham.raku.viewmodel;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import io.dunham.raku.model.File;


public class FileVM {
    private long id;
    private String hash;
    private long size;
    private String filename;
    private String contentType;

    public FileVM() {
    }

    public FileVM(File f) {
        this.id = f.getId();
        this.hash = f.getHash();
        this.size = f.getSize();
        this.filename = f.getFilename();
        this.contentType = f.getContentType();
    }

    public static List<FileVM> mapList(List<File> files) {
        return files.stream()
            .map(FileVM::new)
            .collect(Collectors.toList());
    }

    // --------------------------------------------------

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHash() {
        return this.hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    // --------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;

        if (!(o instanceof FileVM)) {
            return false;
        }

        final FileVM that = (FileVM) o;

        return this.id == that.id &&
            Objects.equals(this.hash, that.hash) &&
            Objects.equals(this.size, that.size) &&
            Objects.equals(this.filename, that.filename) &&
            Objects.equals(this.contentType, that.contentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hash, size, filename, contentType);
    }
}
