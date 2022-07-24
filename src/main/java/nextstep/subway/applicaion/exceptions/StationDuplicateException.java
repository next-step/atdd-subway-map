package nextstep.subway.applicaion.exceptions;

import nextstep.subway.enums.exception.ErrorCode;

public class StationDuplicateException extends BadStationRequestException {
    public StationDuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
