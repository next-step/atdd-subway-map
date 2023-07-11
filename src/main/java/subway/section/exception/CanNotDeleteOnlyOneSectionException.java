package subway.section.exception;

import subway.common.exception.BusinessException;
import subway.common.exception.ErrorCode;

public class CanNotDeleteOnlyOneSectionException extends BusinessException {
    public CanNotDeleteOnlyOneSectionException() {
        super(ErrorCode.CAN_NOT_DELETE_ONLY_ONE_SECTION);
    }
}
