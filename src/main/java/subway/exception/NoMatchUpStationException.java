package subway.exception;

import subway.exception.error.SectionErrorCode;

public class NoMatchUpStationException extends SectionException {

    public NoMatchUpStationException() {
        super(SectionErrorCode.NO_MATCH_UP_STATION);
    }

}
