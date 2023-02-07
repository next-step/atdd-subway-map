package subway.line.exception;

import subway.common.exception.SubwayException;

public class SectionNotFoundException extends SubwayException {

    private static final String MESSAGE = "지하철 구간을 찾을 수 없습니다.";

    public SectionNotFoundException() {
        super(MESSAGE);
    }
}
