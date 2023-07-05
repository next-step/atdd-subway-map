package subway.domain.exception;

import subway.domain.Station;

public class InvalidSectionUpStationException extends IllegalStateException {
    public InvalidSectionUpStationException(Station downStation, Station upStation) {
        super(String.format("%s is not matches %s", downStation.getName(), upStation.getName()));
    }
}
