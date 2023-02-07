package subway.line.exception;

import subway.common.exception.SubwayException;

public class NotSameAsRegisteredDownStation extends SubwayException {

    private static final String MESSAGE = "등록할 구간의 상행 역은 노선에 등록된 하행 종점역이어야 합니다.";

    public NotSameAsRegisteredDownStation() {
        super(MESSAGE);
    }
}
