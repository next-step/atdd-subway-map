package subway.domain.exception;

import subway.domain.Station;

public class InvalidSectionDownStationException extends IllegalStateException {
    public InvalidSectionDownStationException(Station upStation) {
        super(String.format("already registered %s", upStation.getName()));
    }
}
