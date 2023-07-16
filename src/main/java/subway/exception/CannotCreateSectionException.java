package subway.exception;

import subway.exception.error.SectionErrorCode;

public class CannotCreateSectionException extends SectionException {

    public CannotCreateSectionException() {
        super(SectionErrorCode.CANNOT_CREATE_SECTION);
    }
}
