package nextstep.subway.error.exception;

import nextstep.subway.error.ErrorCode;

public class LineNotFoundException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.NOT_LINE_FOUND;
    private String errorMessage = "%s번 노선은 존재하지 않습니다.";

    public LineNotFoundException(Long id) {
        this.errorMessage = String.format(errorMessage, id);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
