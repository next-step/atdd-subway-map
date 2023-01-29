package subway.domain;

public class NotFoundLineException extends RuntimeException {

    public NotFoundLineException(String message) {
        super(message);
    }

}
