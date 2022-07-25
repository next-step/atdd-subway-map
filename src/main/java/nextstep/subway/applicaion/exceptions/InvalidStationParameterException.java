package nextstep.subway.applicaion.exceptions;

import nextstep.subway.enums.exception.ErrorCode;

public class InvalidStationParameterException extends BadStationRequestException {
    public InvalidStationParameterException(ErrorCode errorCode) {
        super(errorCode);
    }
}
