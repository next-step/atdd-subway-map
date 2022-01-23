package nextstep.subway.utils;

public class ExceptionOptional<T extends Exception> {
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static final ExceptionOptional EMPTY = new ExceptionOptional(null) {
        @Override
        public void verify() {}
    };

    private final T exception;

    private ExceptionOptional(T exception) {
        this.exception = exception;
    }

    public static <T extends Exception> ExceptionOptional<T> empty() {
        //noinspection unchecked
        return (ExceptionOptional<T>) EMPTY;
    }

    public static <T extends Exception> ExceptionOptional<T> of(T exception) {
        return new ExceptionOptional<>(exception);
    }

    public void verify() {
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
