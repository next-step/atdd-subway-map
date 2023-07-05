package subway.line.domain.exception;

import subway.global.SubwayException;

public class LineRemoveSectionException extends SubwayException {

    private static final String PREFIX = "구간 삭제 실패) ";

    public LineRemoveSectionException(final String message) {
        super(PREFIX + message);
    }
}
