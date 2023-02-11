package subway.common.exception.line;

import subway.common.exception.InvalidValueException;

public class DownStationCouldNotExistStationExcetion extends InvalidValueException {

    public static final String MESSAGE = "구간의 하행역은 노선에 등록된 역일 수 없습니다.";

    public DownStationCouldNotExistStationExcetion() {
        super(MESSAGE);
    }
}
