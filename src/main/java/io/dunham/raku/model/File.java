package io.dunham.raku.model;

import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.io.Files;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@JsonRootName("file")
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

    public File() {
    }

    public File(String hash, long size, String filename) {
        this.hash = hash;
        this.size = size;
        this.filename = filename;
    }

    public String fileExtension() {
        return Files.getFileExtension(this.filename);
    }
}
