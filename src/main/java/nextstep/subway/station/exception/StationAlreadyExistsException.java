package nextstep.subway.station.exception;

public class StationAlreadyExistsException extends RuntimeException {
    private static final String message = "이미 존재하는 역입니다.";

    public StationAlreadyExistsException() {
        super(message);
    }
}
