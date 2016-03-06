package io.dunham.raku.viewmodel;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import io.dunham.raku.model.Document;


public class DocumentWithEmbeddedTagsVM {
    private long id;
    private String name;
    private Set<FileVM> files;
    private Set<TagVM> tags;

    public DocumentWithEmbeddedTagsVM() {
    }

    public DocumentWithEmbeddedTagsVM(Document d) {
        this.id = d.getId();
        this.name = d.getName();

        if (d.getFiles() != null) {
            this.files = d.getFiles()
                .stream()
                .map(t -> new FileVM(t))
                .collect(Collectors.toSet());
        } else {
            this.files = Sets.newHashSet();
        }

        if (d.getTags() != null) {
            this.tags = d.getTags()
                .stream()
                .map(t -> new TagVM(t))
                .collect(Collectors.toSet());
        } else {
            this.tags = Sets.newHashSet();
        }
    }

    public static List<DocumentWithEmbeddedTagsVM> mapList(List<Document> docs) {
        return docs.stream()
            .map(DocumentWithEmbeddedTagsVM::new)
            .collect(Collectors.toList());
    }

    // --------------------------------------------------

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<FileVM> getFiles() {
        return files;
    }

    public void setFiles(Set<FileVM> files) {
        this.files = files;
    }

    public Set<TagVM> getTags() {
        return this.tags;
    }

    // --------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;

        if (!(o instanceof DocumentWithEmbeddedTagsVM)) {
            return false;
        }

        final DocumentWithEmbeddedTagsVM that = (DocumentWithEmbeddedTagsVM) o;

        return this.id == that.id &&
            Objects.equals(this.name, that.name) &&
            Objects.equals(this.files, that.files) &&
            Objects.equals(this.tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, files, tags);
    }
}