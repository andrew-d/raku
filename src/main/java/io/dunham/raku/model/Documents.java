package io.dunham.raku.model;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonRootName;


@JsonRootName("documents")
public class Documents extends ArrayList<Document> {}
