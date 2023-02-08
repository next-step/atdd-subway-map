package subway.station.exception;

import subway.common.exception.SubwayException;

/**
 * 지하철 역을 찾을 수 없을 때 던지는 예외입니다.
 */
public class StationNotFoundException extends SubwayException {

    private static final String MESSAGE = "지하철 역을 찾을 수 없습니다.";

    public StationNotFoundException() {
        super(MESSAGE);
    }
}
