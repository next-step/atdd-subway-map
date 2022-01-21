package nextstep.subway.exception;

public class NotFoundLineException extends RuntimeException {
    private static final String MESSAGE = "%d id에 해당하는 Line 을 찾을 수 없습니다.";

    public NotFoundLineException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
