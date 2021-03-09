package nextstep.subway.station.exception;

public class StationNotFoundException extends RuntimeException {

    private static final String STATION_NOT_FOUND_EXCEPTION = "지하철 역을 찾을 수 없습니다.";

    public StationNotFoundException() {
        this(STATION_NOT_FOUND_EXCEPTION);
    }

    public StationNotFoundException(String message) {
        super(message);
    }
}
