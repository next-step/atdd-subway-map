package subway.exception;

import javax.persistence.EntityNotFoundException;

public class StationNotFoundException extends EntityNotFoundException {

    private static final String MESSAGE = "존재하지 않는 역입니다.";

    public StationNotFoundException() {
        super(MESSAGE);
    }
}
