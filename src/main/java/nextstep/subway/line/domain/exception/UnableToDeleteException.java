package nextstep.subway.line.domain.exception;

public class UnableToDeleteException extends RuntimeException{
    String message;

    public UnableToDeleteException() {}

    public UnableToDeleteException(String message) {
        this.message = message;
    }
}
