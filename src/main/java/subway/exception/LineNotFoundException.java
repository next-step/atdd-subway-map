package subway.exception;

import subway.exception.error.LineErrorCode;

public class LineNotFoundException extends LineException {

    public LineNotFoundException() {
        super(LineErrorCode.LINE_NOT_FOUND);
    }
}
