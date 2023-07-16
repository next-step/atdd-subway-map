package subway.exception;

import subway.exception.error.SectionErrorCode;

public class SingleSectionDeleteNotAllowedException extends SectionException {

    public SingleSectionDeleteNotAllowedException() {
        super(SectionErrorCode.SINGLE_SECTION_DELETE_NOT_ALLOWED);
    }
}
