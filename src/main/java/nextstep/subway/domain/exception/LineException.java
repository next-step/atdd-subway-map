package nextstep.subway.domain.exception;

public class LineException extends RuntimeException {

    private LineException(String message) {
        super(message);
    }

    public static class NotFound extends LineException {
        public NotFound(Long id) {
            super("Not found line; id=" + id);
        }
    }
}
