package subway.exception;

import subway.exception.error.SectionErrorCode;

public class NoMatchLineUpStationException extends SectionException {

    public NoMatchLineUpStationException() {
        super(SectionErrorCode.NO_MATCH_UP_STATION);
    }

}
