package nextstep.subway.exception;

public class BadRequestException extends IllegalArgumentException {
    public BadRequestException(BadRequestExceptionMessage badRequestExceptionMessage) {
        super(badRequestExceptionMessage.getMessage());
    }
}
