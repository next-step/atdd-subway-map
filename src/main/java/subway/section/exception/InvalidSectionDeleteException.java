package subway.section.exception;

import subway.support.ErrorCode;
import subway.support.SubwayException;

public class InvalidSectionDeleteException extends SubwayException {

//    public InvalidSectionDeleteException() {
//        super(ErrorCode.SECTION_DELETE_FAIL_BY_NOT_ALLOWED_STATION);
//    }
//
//    public InvalidSectionDeleteException(String message) {
//        super(message, ErrorCode.SECTION_DELETE_FAIL_BY_NOT_ALLOWED_STATION);
//    }


    public InvalidSectionDeleteException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidSectionDeleteException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
