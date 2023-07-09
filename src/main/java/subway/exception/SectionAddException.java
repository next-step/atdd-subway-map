package subway.exception;

import static subway.exception.SubwayError.SECTION_ADD_EXCEPTION;

public class SectionAddException extends SubwayException {
    public SectionAddException() {
        super(SECTION_ADD_EXCEPTION);
    }
}
