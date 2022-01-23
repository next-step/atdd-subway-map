package nextstep.subway.common.exception;

import java.util.Objects;

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
