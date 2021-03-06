package nextstep.subway.line.exception;

public class NonExistLineException extends RuntimeException {

    public NonExistLineException(String message) {
        super(message);
    }
}
