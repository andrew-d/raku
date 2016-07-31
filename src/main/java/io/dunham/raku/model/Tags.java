package io.dunham.raku.model;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonRootName;


@JsonRootName("tags")
public class Tags extends ArrayList<Tag> {}
