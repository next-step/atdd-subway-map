package nextstep.subway.exception;

public class NotFoundLineException extends IllegalArgumentException {
    private static final String MESSAGE = "노선이 존재하지 않습니다. 입력값::";

    public NotFoundLineException(long id) {
        super(MESSAGE + id);
    }
}
