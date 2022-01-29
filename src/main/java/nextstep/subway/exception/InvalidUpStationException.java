package nextstep.subway.exception;

public class InvalidUpStationException extends RuntimeException {

    private static final String MSG_UP_STATION_DOES_NOT_BE_CONNECTED = "새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없습니다. name: %s";

    public InvalidUpStationException(String name) {
        super(String.format(MSG_UP_STATION_DOES_NOT_BE_CONNECTED, name));
    }
}
