package nextstep.subway.exception;

public class NotFoundStationException extends RuntimeException {
    private static final String MESSAGE = "해당 (%d) id의 Staion을 찾을 수 없습니다.";

    public NotFoundStationException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
