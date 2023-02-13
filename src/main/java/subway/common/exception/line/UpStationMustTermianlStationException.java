package subway.common.exception.line;

import subway.common.exception.InvalidValueException;

public class UpStationMustTermianlStationException extends InvalidValueException {

    public static final String MESSAGE = "등록하려는 구간의 상행역은 해당 노선의 하행 종점역이어야 합니다.";

    public UpStationMustTermianlStationException() {
        super(MESSAGE);
    }

}
