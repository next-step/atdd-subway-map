package nextstep.subway.applicaion.exceptions;

import nextstep.subway.enums.exception.ErrorCode;

public class DataNotFoundException extends AbstractException{
    public DataNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
