package nextstep.subway.exception;

public class DuplicateStationException extends RuntimeException {
    private static final String message = "이미 존재하는 지하철역입니다.";

    public DuplicateStationException() {
        super(message);
    }
}
