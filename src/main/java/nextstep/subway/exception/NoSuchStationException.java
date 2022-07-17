package nextstep.subway.exception;

public class NoSuchStationException extends RuntimeException {

    private static final String NO_SUCH_STATION_EXCEPTION = "요청한 역은 존재하지 않는 역입니다. (요청한 id: %d)";

    public NoSuchStationException(long stationId) {
        super(String.format(NO_SUCH_STATION_EXCEPTION, stationId));
    }

}