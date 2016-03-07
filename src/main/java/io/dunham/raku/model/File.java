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

import lombok.EqualsAndHashCode;
import lombok.Setter;


@Entity
@Table(name = "files")
@Access(AccessType.PROPERTY)
@EqualsAndHashCode
public class File {
    @Setter private long id;
    @Setter private String hash;
    @Setter private long size;
    @Setter private String filename;
    @Setter private String contentType;
    @Setter private Document document;

    public File() {
    }

    public File(String hash, long size, String filename, Document document) {
        this.hash = hash;
        this.size = size;
        this.filename = filename;
        this.document = document;
    }

    @Id
    @Column(name = "file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    @Column(name = "hash", nullable = false, length = 64)
    public String getHash() {
        return hash;
    }

    @Column(name = "size", nullable = false)
    public long getSize() {
        return size;
    }

    @Column(name = "filename", nullable = false, length = 256)
    public String getFilename() {
        return filename;
    }

    @Column(name = "content_type", nullable = true, length = 256)
    public String getContentType() {
        return contentType;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "document_id", nullable = false)
    public Document getDocument() {
        return this.document;
    }
}
