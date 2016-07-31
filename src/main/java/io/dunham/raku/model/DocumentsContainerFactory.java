package io.dunham.raku.model;

import org.skife.jdbi.v2.ContainerBuilder;
import org.skife.jdbi.v2.tweak.ContainerFactory;


public class DocumentsContainerFactory implements ContainerFactory<Documents> {

    @Override
    public boolean accepts(Class<?> type) {
        return type.equals(Document.class);
    }

    @Override
    public ContainerBuilder<Documents> newContainerBuilderFor(Class<?> type) {
        return new DocumentsContainerBuilder();
    }

    private static class DocumentsContainerBuilder implements ContainerBuilder<Documents> {

        private Documents documents = new Documents();

        @Override
        public ContainerBuilder<Documents> add(Object it) {
            documents.add((Document) it);
            return this;
        }

        @Override
        public Documents build() {
            return documents;
        }
    }
}
