package nextstep.subway.exception;

public class NotFoundStationException extends RuntimeException {
    private final static String STATION_ID_NOT_FOUND_MESSAGE = "id %d의 Station을 찾을 수 없습니다.";

    public NotFoundStationException(Long id) {
        super(String.format(STATION_ID_NOT_FOUND_MESSAGE, id));
    }
}
