package nextstep.subway.station.exception;

public class StationNameDuplicatedException extends RuntimeException {

    private static final String STATION_NAME_DUPLICATED_EXCEPTION = "지하철역 이름은 중복될 수 없습니다.";

    public StationNameDuplicatedException() {
        this(STATION_NAME_DUPLICATED_EXCEPTION);
    }

    public StationNameDuplicatedException(String message) {
        super(message);
    }
}
