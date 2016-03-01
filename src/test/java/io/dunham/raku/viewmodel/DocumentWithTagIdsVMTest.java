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


public class DocumentWithTagIdsVMTest {
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

        DocumentWithTagIdsVM vm = new DocumentWithTagIdsVM(doc);
        assertThat(vm.getId()).isEqualTo(1234);
        assertThat(vm.getName()).isEqualTo("foobar");
        assertThat(vm.getTags()).isNotNull();

        List<Tag> sortedTags = Lists.newArrayList(tags);
        List<Long> sortedTagIds = Lists.newArrayList(vm.getTags());

        Collections.sort(sortedTags, (x, y) -> Long.compare(x.getId(), y.getId()));
        Collections.sort(sortedTagIds);

        // Same number of elements
        assertThat(sortedTags).hasSameSizeAs(sortedTagIds);

        // Each element is the same.
        for (int i = 0; i < sortedTags.size(); i++) {
            assertThat(sortedTagIds.get(i))
                .isEqualTo(sortedTags.get(i).getId());
        }
    }

    @Test
    public void includesOnlyIds() throws Exception {
        Document doc = new Document();
        doc.setId(1234);
        doc.setName("foobar");
        doc.setTags(tags);

        DocumentWithTagIdsVM vm = new DocumentWithTagIdsVM(doc);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(vm);

        // Read back in as a thing that can read tags.
        ObjectReader reader = new ObjectMapper().reader(WithTags.class);
        WithTags to = reader.readValue(json);

        List<Long> givenTagIds = Lists.newArrayList(to.getTags());
        Collections.sort(givenTagIds);

        // Same number of elements, IDs match.
        assertThat(givenTagIds).hasSameSizeAs(tags);
        assertThat(givenTagIds.get(0)).isEqualTo(1);
        assertThat(givenTagIds.get(1)).isEqualTo(2);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class WithTags {
        private Set<Long> tags;

        public WithTags() {
        }

        public Set<Long> getTags() {
            return this.tags;
        }

        public void setTags(Set<Long> tags) {
            this.tags = tags;
        }
    }
}
