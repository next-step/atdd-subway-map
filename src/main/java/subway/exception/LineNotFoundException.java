package subway.exception;

import javax.persistence.EntityNotFoundException;

public class LineNotFoundException extends EntityNotFoundException {

    private static final String MESSAGE = "존재하지 않는 노선입니다.";

    public LineNotFoundException() {
        super(MESSAGE);
    }
}
