package io.dunham.raku.viewmodel;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import io.dunham.raku.model.Document;


public class DocumentWithTagIdsVM {
    private long id;
    private String name;
    private Set<Long> tags;

    public DocumentWithTagIdsVM() {
    }

    public DocumentWithTagIdsVM(Document d) {
        this.id = d.getId();
        this.name = d.getName();
        this.tags = d.getTags()
            .stream()
            .map(t -> t.getId())
            .collect(Collectors.toSet());
    }

    public static List<DocumentWithTagIdsVM> mapList(List<Document> docs) {
        return docs.stream()
            .map(DocumentWithTagIdsVM::new)
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

    public Set<Long> getTags() {
        return this.tags;
    }

    // --------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;

        if (!(o instanceof DocumentWithTagIdsVM)) {
            return false;
        }

        final DocumentWithTagIdsVM that = (DocumentWithTagIdsVM) o;

        return this.id == that.id &&
            this.name == that.name &&
            Objects.equals(this.tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tags);
    }
}
