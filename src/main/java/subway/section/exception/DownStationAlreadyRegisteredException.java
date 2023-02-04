package subway.section.exception;

import subway.common.exception.SubwayException;

public class DownStationAlreadyRegisteredException extends SubwayException {

    private static final String MESSAGE = "등록할 구간의 하행 역은 노선에 등록되지 않은 역이어야 합니다.";

    public DownStationAlreadyRegisteredException() {
        super(MESSAGE);
    }
}
