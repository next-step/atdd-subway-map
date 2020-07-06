package nextstep.subway.line.domain;

public class LineNotFoundException extends RuntimeException {

    public LineNotFoundException() {
        super();
    }

    public LineNotFoundException(String message) {
        super(message);
    }

    public LineNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public LineNotFoundException(Throwable cause) {
        super(cause);
    }

}
