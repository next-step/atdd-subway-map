package subway.station;

import subway.exception.BusinessException;
import subway.exception.ErrorMessage;

public class StationNotFoundException extends BusinessException {

    private static final ErrorMessage ERROR_MESSAGE = ErrorMessage.LINE_NOT_FOUND_EXCEPTION;

    public StationNotFoundException() {
        super(ERROR_MESSAGE.getMessage());
    }
}
