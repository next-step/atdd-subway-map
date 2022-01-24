package nextstep.subway.domain.exception;

import nextstep.subway.domain.Line;

public class LineException extends RuntimeException {

    private LineException(String message) {
        super(message);
    }

    public static class NotFound extends LineException {
        public NotFound(Long id) {
            super("Not found line; id=" + id);
        }
    }

    public static class Duplicated extends LineException {
        public Duplicated(Line line) {
            super("Already exists line; name=" + line.getName());
        }
    }
}
