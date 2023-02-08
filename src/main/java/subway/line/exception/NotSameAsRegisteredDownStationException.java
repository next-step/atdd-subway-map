package subway.line.exception;

import subway.common.exception.SubwayException;

/**
 * 등록할 구간의 상행역이 해당 노선의 하행 종점역과 다를 때 던지는 예외입니다.
 */
public class NotSameAsRegisteredDownStationException extends SubwayException {

    private static final String MESSAGE = "등록할 구간의 상행 역은 노선에 등록된 하행 종점역이어야 합니다.";

    public NotSameAsRegisteredDownStationException() {
        super(MESSAGE);
    }
}
