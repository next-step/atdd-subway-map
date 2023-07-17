package subway.exception.impl;

import subway.exception.SubwayException;
import subway.exception.error.SubwayErrorCode;

public class SubwayNotFoundException extends SubwayException {

    public SubwayNotFoundException() {
        super(SubwayErrorCode.LINE_NOT_FOUND);
    }
}
