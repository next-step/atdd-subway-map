package nextstep.subway.error.dto;

import nextstep.subway.error.ErrorCode;

public class ErrorResponse {
    private int status;
    private String errorCode;
    private String errorMessage;

    public ErrorResponse(ErrorCode errorCode, String errorMessage) {
        this.status = errorCode.getStatus();
        this.errorCode = errorCode.getErrorCode();
        this.errorMessage = errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
