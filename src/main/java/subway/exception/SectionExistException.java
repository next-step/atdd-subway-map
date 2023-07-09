package subway.exception;

import static subway.exception.SubwayError.SECTION_EXIST_EXCEPTION;

public class SectionExistException extends SubwayException {
    public SectionExistException() {
        super(SECTION_EXIST_EXCEPTION);
    }
}
