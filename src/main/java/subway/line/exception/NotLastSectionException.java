package subway.line.exception;

import subway.common.exception.SubwayException;

/**
 * 삭제할 구간이 마지막 구간이 아닐 때 던지는 예외입니다.
 */
public class NotLastSectionException extends SubwayException {

    private static final String MESSAGE = "삭제할 구간은 노선의 마지막 구간이어야 합니다.";

    public NotLastSectionException() {
        super(MESSAGE);
    }
}
