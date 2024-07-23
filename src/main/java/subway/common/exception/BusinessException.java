package subway.common.exception;

import lombok.Getter;
import subway.common.response.ErrorCode;

public class BusinessException extends RuntimeException {
    @Getter
    private ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }



}
