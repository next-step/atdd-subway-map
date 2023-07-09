package subway.exception;

import static subway.exception.SubwayError.NOT_FOUND_SECTION;

public class SectionNotFoundException extends SubwayException {
    public SectionNotFoundException() {
        super(NOT_FOUND_SECTION);
    }
}
