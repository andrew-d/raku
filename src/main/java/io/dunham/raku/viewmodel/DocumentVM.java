package io.dunham.raku.viewmodel;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import io.dunham.raku.model.Document;


public class DocumentVM {
    private long id;
    private String name;

    public DocumentVM() {
    }

    public DocumentVM(Document d) {
        this.id = d.getId();
        this.name = d.getName();
    }

    public static List<DocumentVM> mapList(List<Document> docs) {
        return docs.stream()
            .map(DocumentVM::new)
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

    // --------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;

        if (!(o instanceof DocumentVM)) {
            return false;
        }

        final DocumentVM that = (DocumentVM) o;

        return this.id == that.id && this.name == that.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
