package subway.section.exception;

import subway.support.ErrorCode;
import subway.support.SubwayException;

public class InvalidSectionUpStationException extends SubwayException {

    public InvalidSectionUpStationException() {
        super(ErrorCode.SECTION_CREATE_FAIL_BY_UPSTATION);
    }
}
