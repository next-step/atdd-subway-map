package subway.common.exception.station;

import subway.common.exception.EntityNotFound;

public class StationNotFound extends EntityNotFound {
    private static final String MESSAGE = "지하철 역을 찾을 수 없습니다.";

    public StationNotFound() {
        super(MESSAGE);
    }
}
