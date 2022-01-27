package nextstep.subway.domain.exception;

public class CannotRemoveSectionException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "구간이 하나만 있는 경우 삭제할 수 없습니다.";
    private static final String LAST_STATION_MESSAGE = "하행 종점역만 삭제할 수 있습니다. %s은 삭제할 수 없습니다.";

    public CannotRemoveSectionException() {
        super(DEFAULT_MESSAGE);
    }

    public CannotRemoveSectionException(String stationName) {
        super(String.format(LAST_STATION_MESSAGE, stationName));
    }
}
