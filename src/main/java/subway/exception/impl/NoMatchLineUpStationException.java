package subway.exception.impl;

import subway.exception.SubwayException;
import subway.exception.error.SubwayErrorCode;

public class NoMatchLineUpStationException extends SubwayException {

    public NoMatchLineUpStationException() {
        super(SubwayErrorCode.NO_MATCH_UP_STATION);
    }

}
