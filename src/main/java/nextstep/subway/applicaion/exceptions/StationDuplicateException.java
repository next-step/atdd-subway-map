package nextstep.subway.applicaion.exceptions;

import nextstep.subway.enums.exception.ErrorCode;

public class StationDuplicateException extends AbstractException{
    public StationDuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
