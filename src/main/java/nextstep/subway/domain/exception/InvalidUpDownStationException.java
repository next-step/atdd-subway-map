package nextstep.subway.domain.exception;

public class InvalidUpDownStationException extends IllegalArgumentException {
    private static final String MESSAGE = "상행 하생 지하철역이 동일할 수 없습니다.";

    public InvalidUpDownStationException() {
        super(MESSAGE);
    }
}
