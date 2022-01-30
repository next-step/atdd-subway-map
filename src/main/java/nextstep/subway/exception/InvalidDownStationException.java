package nextstep.subway.exception;

public class InvalidDownStationException extends RuntimeException {

    private static final String MSG_DOWN_STATION_EXISTS = "새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없습니다. name: %s";

    public InvalidDownStationException(String name) {
        super(String.format(MSG_DOWN_STATION_EXISTS, name));
    }
}
