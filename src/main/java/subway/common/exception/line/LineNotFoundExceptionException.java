package subway.common.exception.line;

import subway.common.exception.EntityNotFoundException;

public class LineNotFoundExceptionException extends EntityNotFoundException {

    private static final String MESSAGE = "지하철 노선을 찾을 수 없습니다.";

    public LineNotFoundExceptionException() {
        super(MESSAGE);
    }
}
