package subway.line.exception;

import subway.common.exception.SubwayException;

public class LineNotFoundException extends SubwayException {
    private static final String MESSAGE = "지하철 노선을 찾을 수 없습니다.";

    public LineNotFoundException() {
        super(MESSAGE);
    }
}
