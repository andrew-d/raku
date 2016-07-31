package io.dunham.raku.model;

import org.skife.jdbi.v2.ContainerBuilder;
import org.skife.jdbi.v2.tweak.ContainerFactory;


public class TagsContainerFactory implements ContainerFactory<Tags> {

    @Override
    public boolean accepts(Class<?> type) {
        return type.equals(Tag.class);
    }

    @Override
    public ContainerBuilder<Tags> newContainerBuilderFor(Class<?> type) {
        return new TagsContainerBuilder();
    }

    private static class TagsContainerBuilder implements ContainerBuilder<Tags> {

        private Tags tags = new Tags();

        @Override
        public ContainerBuilder<Tags> add(Object it) {
            tags.add((Tag) it);
            return this;
        }

        @Override
        public Tags build() {
            return tags;
        }
    }
}
