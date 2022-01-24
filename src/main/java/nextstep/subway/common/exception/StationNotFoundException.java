package nextstep.subway.common.exception;

import java.util.NoSuchElementException;

import static nextstep.subway.common.ErrorMessages.NOT_FOUND_STATION;

public class StationNotFoundException extends NoSuchElementException {
    public StationNotFoundException() {
        super(NOT_FOUND_STATION.getMessage());
    }
}
