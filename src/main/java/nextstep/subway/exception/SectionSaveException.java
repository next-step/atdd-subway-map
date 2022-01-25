package nextstep.subway.exception;

import nextstep.subway.error.ErrorCode;

public class SectionSaveException extends NextStepException {

    public SectionSaveException(String message) {
        super(ErrorCode.SECTION_CREATE_ERROR, message);
    }
}
