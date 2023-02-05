package subway.section.exception;

import subway.common.exception.SubwayException;

public class NotLastSectionException extends SubwayException {

    private static final String MESSAGE = "삭제할 구간은 노선의 마지막 구간이어야 합니다.";

    public NotLastSectionException() {
        super(MESSAGE);
    }
}
