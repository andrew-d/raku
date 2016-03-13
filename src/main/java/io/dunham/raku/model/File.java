package io.dunham.raku.model;

import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@EqualsAndHashCode
@ToString
public class File {
    @Getter @Setter private long id;

    @Getter @Setter
    @NotNull
    @Size(min = 1, max = 64)
    private String hash;

    @Getter @Setter
    @Min(1)
    private long size;

    @Getter @Setter
    @NotNull
    @Size(min = 1, max = 256)
    private String filename;

    @Getter @Setter
    @Size(min = 1, max = 256)
    private String contentType;

    @Getter @Setter
    private long documentId;

    public File() {
    }

    public File(String hash, long size, String filename, long documentId) {
        this.hash = hash;
        this.size = size;
        this.filename = filename;
        this.documentId = documentId;
    }
}
