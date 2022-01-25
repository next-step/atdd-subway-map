package nextstep.subway.exception;

public class DeleteLastDownStationException extends RuntimeException {

    private static final String message = "마지막역(하행 종점역)만 삭제 가능합니다. - %s";

    public DeleteLastDownStationException(String stationName) {
        super(String.format(message, stationName));

    }

}
