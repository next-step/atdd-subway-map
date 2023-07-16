package subway.exception;

import subway.exception.error.SectionErrorCode;

public class NonLastStationDeleteNotAllowedException extends SectionException {

    public NonLastStationDeleteNotAllowedException() {
        super(SectionErrorCode.NON_LAST_STATION_DELETE_NOT_ALLOWED);
    }
}
