package com.github.bdpiparva.plugin.base.test_helper.annotations;

import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(JsonSourceProvider.class)
public @interface JsonSource {
    String[] jsonFiles();
}