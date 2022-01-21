package nextstep.subway.exception;

public class NotFoundLineException extends RuntimeException {
    private final static String LINE_ID_NOT_FOUND_MESSAGE = "id %d의 Line을 찾을 수 없습니다.";

    public NotFoundLineException(Long id) {
        super(String.format(LINE_ID_NOT_FOUND_MESSAGE, id));
    }
}
