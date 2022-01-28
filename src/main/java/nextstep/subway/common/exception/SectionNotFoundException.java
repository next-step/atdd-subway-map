package nextstep.subway.common.exception;

import java.util.NoSuchElementException;

import static nextstep.subway.common.ErrorMessages.NOT_FOUND_SECTION;

public class SectionNotFoundException extends NoSuchElementException {
    public SectionNotFoundException() {
        super(NOT_FOUND_SECTION.getMessage());
    }
}
