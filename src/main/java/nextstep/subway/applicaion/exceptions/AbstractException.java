package nextstep.subway.applicaion.exceptions;

import lombok.Getter;
import nextstep.subway.enums.exception.ErrorCode;

@Getter
public abstract class AbstractException extends RuntimeException{
    private final ErrorCode errorCode;

    public AbstractException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
