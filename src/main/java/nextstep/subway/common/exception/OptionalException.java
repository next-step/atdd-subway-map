package nextstep.subway.common.exception;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class OptionalException<T extends Exception> {
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static final OptionalException EMPTY = new OptionalException(null);

    private final T exception;

    private OptionalException(T exception) {
        this.exception = exception;
    }

    public static <T extends Exception> OptionalException<T> empty() {
        //noinspection unchecked
        return (OptionalException<T>) EMPTY;
    }

    public static <T extends Exception> OptionalException<T> of(T exception) {
        return new OptionalException<>(exception);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T extends Exception> OptionalException<T> ifPresent(Optional<?> optional, Supplier<T> exception) {
        if (optional.isPresent()) {
            return of(exception.get());
        }
        return empty();
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T extends Exception> OptionalException<T> ifEmpty(Optional<?> optional, Supplier<T> exception) {
        if (optional.isPresent()) {
            return empty();
        }
        return of(exception.get());
    }

    public void verify() {
        if (Objects.isNull(exception)) {
            return;
        }
        if (exception instanceof RuntimeException) {
            throw (RuntimeException) exception;
        }
        try {
            throw exception;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
