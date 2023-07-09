package subway.section.exception;

import subway.common.exception.BusinessException;
import subway.common.exception.ErrorCode;

public class DeleteOnlyTerminusStationException extends BusinessException {
    public DeleteOnlyTerminusStationException() {
        super(ErrorCode.DELETE_ONLY_TERMINUS_STATION);
    }
}
