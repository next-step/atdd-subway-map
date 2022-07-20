package nextstep.subway.applicaion.exceptions;

import nextstep.subway.enums.exception.ErrorCode;

public class SectionArrayOutOfBoundException extends AbstractException{
    public SectionArrayOutOfBoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
