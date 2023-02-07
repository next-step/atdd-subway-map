package subway.line.exception;

import subway.common.exception.SubwayException;

/**
 * 등록할 하행역이 이미 노선에 등록되어 있을 때 던지는 예외입니다.
 */
public class DownStationAlreadyRegisteredException extends SubwayException {

    private static final String MESSAGE = "등록할 구간의 하행 역은 노선에 등록되지 않은 역이어야 합니다.";

    public DownStationAlreadyRegisteredException() {
        super(MESSAGE);
    }
}
