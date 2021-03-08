package nextstep.subway.station.exception;

public class NotFoundStationException extends NullPointerException {
    private static final String MESSAGE = "존재하지 않는 지하철 역에 대한 요청입니다.";

    public NotFoundStationException() {
        super(MESSAGE);
    }
}
