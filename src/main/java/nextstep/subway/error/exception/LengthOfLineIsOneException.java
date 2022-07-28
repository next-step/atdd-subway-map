package nextstep.subway.error.exception;

import nextstep.subway.error.ErrorCode;

public class LengthOfLineIsOneException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.LENGTH_OF_LINE_IS_ONE_ERR;
    private String errorMessage = "%s번 노선의 구간은 유일하므로 삭제할 수 없습니다.";

    public LengthOfLineIsOneException(Long id) {
        this.errorMessage = String.format(errorMessage, id);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
