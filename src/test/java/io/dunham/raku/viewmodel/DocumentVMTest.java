package io.dunham.raku.viewmodel;

import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

import io.dunham.raku.model.Document;
import io.dunham.raku.model.Tag;


public class DocumentVMTest {
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

        DocumentVM vm = new DocumentVM(doc);
        assertThat(vm.getId()).isEqualTo(1234);
        assertThat(vm.getName()).isEqualTo("foobar");
    }

    @Test
    public void doesNotIncludeTags() throws Exception {
        Document doc = new Document();
        doc.setId(1234);
        doc.setName("foobar");
        doc.setTags(tags);

        DocumentVM vm = new DocumentVM(doc);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(vm);

        assertThat(json).doesNotContain("tags");
    }
}
