package io.dunham.raku.viewmodel;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import io.dunham.raku.model.File;


public class FileVM {
    private long id;
    private String hash;

    public FileVM() {
    }

    public FileVM(File f) {
        this.id = f.getId();
        this.hash = f.getHash();
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

    // --------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;

        if (!(o instanceof FileVM)) {
            return false;
        }

        final FileVM that = (FileVM) o;

        return this.id == that.id && Objects.equals(this.hash, that.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hash);
    }
}
