package subway.common.exception.station;

import subway.common.exception.EntityNotFoundException;

public class StationNotFoundExceptionException extends EntityNotFoundException {
    private static final String MESSAGE = "지하철 역을 찾을 수 없습니다.";

    public StationNotFoundExceptionException() {
        super(MESSAGE);
    }
}
