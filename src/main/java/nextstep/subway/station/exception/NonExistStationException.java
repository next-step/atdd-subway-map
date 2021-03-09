package nextstep.subway.station.exception;

import nextstep.subway.common.exception.NonExistResourceException;

public class NonExistStationException extends NonExistResourceException {

    public NonExistStationException(String message) {
        super(message);
    }
}
