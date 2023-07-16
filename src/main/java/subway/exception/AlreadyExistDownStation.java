package subway.exception;

import subway.exception.error.SectionErrorCode;

public class AlreadyExistDownStation extends SectionException {

    public AlreadyExistDownStation() {
        super(SectionErrorCode.ALREADY_EXIST_DOWN_STATION);
    }

}
