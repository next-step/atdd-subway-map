package subway.exception;

import subway.exception.error.SectionErrorCode;

public class SingleSectionDeleteNotAllowed extends SectionException {

    public SingleSectionDeleteNotAllowed() {
        super(SectionErrorCode.SINGLE_SECTION_DELETE_NOT_ALLOWED);
    }
}
