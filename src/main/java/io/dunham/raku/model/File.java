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
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "files")
@Access(AccessType.PROPERTY)
public class File {
    private long id;
    private String hash;
    private Document document;

    public File() {
    }

    public File(String hash, Document document) {
        this.hash = hash;
        this.document = document;
    }

    @Id
    @Column(name = "file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "hash", nullable = false, length = 64)
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "document_id", nullable = false)
    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof File)) {
            return false;
        }

        final File that = (File) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.hash, that.hash) &&
                Objects.equals(this.document, that.document);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, hash, document);
    }
}
