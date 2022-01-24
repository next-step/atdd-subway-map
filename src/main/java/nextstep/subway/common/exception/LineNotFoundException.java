package nextstep.subway.common.exception;

import java.util.NoSuchElementException;

import static nextstep.subway.common.ErrorMessages.NOT_FOUND_LINE;

public class LineNotFoundException extends NoSuchElementException {
    public LineNotFoundException() {
        super(NOT_FOUND_LINE.getMessage());
    }
}