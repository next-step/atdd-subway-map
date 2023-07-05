package subway.section.exception;

import subway.support.ErrorCode;
import subway.support.SubwayException;

public class InvalidSectionUpstationException extends SubwayException {

    public InvalidSectionUpstationException() {
        super(ErrorCode.SECTION_CREATE_FAIL_BY_UPSTATION);
    }
}
