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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Setter;


@Entity
@Table(name = "tags")
@Access(AccessType.PROPERTY)
@EqualsAndHashCode(exclude = {"documents"})
public class Tag {
    @Setter private long id;
    @Setter private String name;
    @Setter private Set<Document> documents;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "document_tags",
        joinColumns=@JoinColumn(name = "tag_id", nullable = false),
        inverseJoinColumns=@JoinColumn(name = "document_id", nullable = false)
    )
    public Set<Document> getDocuments() {
        return documents;
    }
}
