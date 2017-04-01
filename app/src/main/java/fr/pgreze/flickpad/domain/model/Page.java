package fr.pgreze.flickpad.domain.model;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class Page<T> {

    public abstract int currentPage();

    public abstract int pages();

    public abstract int perPage();

    public abstract int total();

    public abstract List<T> items();

    public static <T> Page<T> create(int currentPage, int pages, int perPage, int total, List<T> items) {
        return new AutoValue_Page<>(currentPage, pages, perPage, total, items);
    }
}
