package subway.exception;

import javax.persistence.EntityNotFoundException;

public class SectionNotFoundException extends EntityNotFoundException {

    private static final String MESSAGE = "존재하지 않는 구간입니다.";

    public SectionNotFoundException() {
        super(MESSAGE);
    }
}
