package com.amrhefny.jobtracker.util;

import jakarta.validation.constraints.NotNull;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public final class PatchField<T> {
    private static final PatchField<?> NOT_PROVIDED = new PatchField<>(null, false);

    private final T value;
    private final boolean provided;

    private PatchField(T value, boolean provided) {
        this.value = value;
        this.provided = provided;
    }

    public static <T> PatchField<T> of(T value) {
        return new PatchField<>(value, true);
    }

    public static <T> PatchField<T> notProvided() {
        return (PatchField<T>) NOT_PROVIDED;
    }

    public boolean isProvided() {
        return provided;
    }

    public T get() {
        if (!provided) {
            throw new NoSuchElementException("No value provided");
        }
        return value;
    }

    public void ifProvided(@NotNull Consumer<? super T> action) {
        Objects.requireNonNull(action);
        if (provided) {
            action.accept(value);
        }
    }

}
