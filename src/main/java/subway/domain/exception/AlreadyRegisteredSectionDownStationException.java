package subway.domain.exception;

import subway.domain.Station;

public class AlreadyRegisteredSectionDownStationException extends IllegalStateException {
    public AlreadyRegisteredSectionDownStationException(Station upStation) {
        super(String.format("already registered %s", upStation.getName()));
    }
}
