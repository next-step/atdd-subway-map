package subway.line;

import subway.exception.BusinessException;
import subway.exception.ErrorCode;

public class NonDownstreamTerminusException extends BusinessException {

    private static final ErrorCode errorCode = ErrorCode.NON_DOWNSTREAM_TERMINUS_EXCEPTION;

    public NonDownstreamTerminusException() {
        super(errorCode.getMessage());
    }
}
