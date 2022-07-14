package nextstep.subway.exception.unchecked;

import nextstep.subway.exception.ErrorCode;

public class SectionException extends BusinessException {
    public SectionException(ErrorCode errorCode) {
        super(errorCode);
    }
}
