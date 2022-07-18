package nextstep.subway.station.domain.exception;

import nextstep.subway.common.EntityNotFoundException;

public class StationNotFoundException extends EntityNotFoundException {

    public StationNotFoundException(long stationId) {
        super(String.format("Can't find line with station of %d", stationId));
    }
}
