package io.dunham.raku.viewmodel;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import io.dunham.raku.model.File;


@EqualsAndHashCode
@ToString
public class FileVM {
    @Getter @Setter private long id;
    @Getter @Setter private String hash;
    @Getter @Setter private long size;
    @Getter @Setter private String filename;
    @Getter @Setter private String contentType;

    public FileVM() {
    }

    public FileVM(File f) {
        this.id = f.getId();
        this.hash = f.getHash();
        this.size = f.getSize();
        this.filename = f.getFilename();
        this.contentType = f.getContentType();
    }

    public static List<FileVM> mapList(List<File> files) {
        return files.stream()
            .map(FileVM::new)
            .collect(Collectors.toList());
    }
}
