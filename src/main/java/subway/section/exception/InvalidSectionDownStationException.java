package subway.section.exception;

import subway.support.ErrorCode;
import subway.support.SubwayException;

public class InvalidSectionDownStationException extends SubwayException {

    public InvalidSectionDownStationException() {
        super(ErrorCode.SECTION_CREATE_FAIL_BY_DOWNSTATION);
    }
}
