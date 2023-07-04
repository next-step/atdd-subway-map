package subway.line.exception;

import subway.common.exception.BusinessException;
import subway.common.exception.ErrorCode;

public class LineNotFoundException extends BusinessException {
    public LineNotFoundException() {
        super(ErrorCode.LINE_NOT_FOUND_EXCEPTION);
    }
}
