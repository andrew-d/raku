package io.dunham.raku.model;

import java.util.Objects;
import java.util.Set;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Setter;


@Entity
@Table(name = "documents")
@Access(AccessType.PROPERTY)
@EqualsAndHashCode(exclude = {"tags", "files"})
public class Document {
    @Setter private long id;
    @Setter private String name;
    @Setter private Set<Tag> tags;
    @Setter private Set<File> files;

    public Document() {
    }

    public Document(String name, Set<Tag> tags, Set<File> files) {
        this.name = name;
        this.tags = tags;
        this.files = files;
    }

    @Id
    @Column(name = "document_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    @Column(name = "name", nullable = false, length = 256)
    public String getName() {
        return name;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "documents")
    public Set<Tag> getTags() {
        return tags;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "document")
    public Set<File> getFiles() {
        return files;
    }
}
