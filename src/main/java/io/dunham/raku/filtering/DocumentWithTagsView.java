package io.dunham.raku.filtering;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.glassfish.hk2.api.AnnotationLiteral;
import org.glassfish.jersey.message.filtering.EntityFiltering;


@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@EntityFiltering
public @interface DocumentWithTagsView {
    public static class Factory extends AnnotationLiteral<DocumentWithTagsView> implements DocumentWithTagsView {
        private Factory() {
        }

        public static DocumentWithTagsView get() {
            return new Factory();
        }
    }
}
