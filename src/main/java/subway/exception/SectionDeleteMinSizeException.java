package subway.exception;

import static subway.exception.SubwayError.SECTION_DELETE_MIN_SIZE_EXCEPTION;

public class SectionDeleteMinSizeException extends SubwayException {
    public SectionDeleteMinSizeException() {
        super(SECTION_DELETE_MIN_SIZE_EXCEPTION);
    }
}
