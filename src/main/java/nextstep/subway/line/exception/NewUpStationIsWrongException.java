package nextstep.subway.line.exception;

public class NewUpStationIsWrongException extends RuntimeException {

    private static final String EXCEPTION_DESCRIPTION = "새로운 구간의 상행역이 잘못되었습니다.";

    public NewUpStationIsWrongException() {
        this(EXCEPTION_DESCRIPTION);
    }

    public NewUpStationIsWrongException(String message) {
        super(message);
    }
}
