package io.dunham.raku.viewmodel;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

import io.dunham.raku.model.Document;
import io.dunham.raku.model.Tag;


public class DocumentWithEmbeddedTagsVMTest {
    private Set<Tag> tags;

    @Before
    public void initTags() {
        Tag t1 = new Tag("foo");
        Tag t2 = new Tag("bar");

        t1.setId(1);
        t2.setId(2);

        tags = Sets.newHashSet(t1, t2);
    }

    @Test
    public void willInitializeFromDocument() {
        Document doc = new Document();
        doc.setId(1234);
        doc.setName("foobar");
        doc.setTags(tags);

        DocumentWithEmbeddedTagsVM vm = new DocumentWithEmbeddedTagsVM(doc);
        assertThat(vm.getId()).isEqualTo(1234);
        assertThat(vm.getName()).isEqualTo("foobar");
        assertThat(vm.getTags()).isNotNull();

        List<Tag> sortedTags = Lists.newArrayList(tags);
        List<TagVM> sortedTagVMs = Lists.newArrayList(vm.getTags());

        Collections.sort(sortedTags, (x, y) -> x.getName().compareTo(y.getName()));
        Collections.sort(sortedTagVMs, (x, y) -> x.getName().compareTo(y.getName()));

        // Same number of elements
        assertThat(sortedTags).hasSameSizeAs(sortedTagVMs);

        // Each element is the same.
        for (int i = 0; i < sortedTags.size(); i++) {
            Object t = sortedTags.get(i);
            Object tvm = sortedTagVMs.get(i);

            assertThat(t).isEqualToComparingOnlyGivenFields(tvm, "id", "name");
        }
    }

    @Test
    public void includesFullTags() throws Exception {
        Document doc = new Document();
        doc.setId(1234);
        doc.setName("foobar");
        doc.setTags(tags);

        DocumentWithEmbeddedTagsVM vm = new DocumentWithEmbeddedTagsVM(doc);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(vm);

        // Read back in as a thing that can read tags.
        ObjectReader reader = new ObjectMapper().reader(WithTags.class);
        WithTags to = reader.readValue(json);

        // Load and sort tags.
        List<Tag> expectedTags = Lists.newArrayList(tags);
        List<Tag> givenTags = Lists.newArrayList(to.getTags());

        Collections.sort(expectedTags, (x, y) -> x.getName().compareTo(y.getName()));
        Collections.sort(givenTags, (x, y) -> x.getName().compareTo(y.getName()));

        // Same number of elements
        assertThat(givenTags).hasSameSizeAs(expectedTags);

        // Each element is the same, but 'documents' field should be null.
        for (int i = 0; i < expectedTags.size(); i++) {
            Tag exp = expectedTags.get(i);
            Tag giv = givenTags.get(i);

            assertThat(giv).isEqualToComparingOnlyGivenFields(exp, "id", "name");
            assertThat(giv.getDocuments()).isNull();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class WithTags {
        private Set<Tag> tags;

        public WithTags() {
        }

        public Set<Tag> getTags() {
            return this.tags;
        }

        public void setTags(Set<Tag> tags) {
            this.tags = tags;
        }
    }
}
