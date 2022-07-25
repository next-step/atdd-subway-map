package nextstep.subway.applicaion.exceptions;

import lombok.Getter;
import nextstep.subway.enums.exception.ErrorCode;

@Getter
public abstract class BadStationRequestException extends RuntimeException{
    private final ErrorCode errorCode;

    public BadStationRequestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
