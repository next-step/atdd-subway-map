package subway.common.exception.line;

import subway.common.exception.InvalidValueException;

public class InvalidLineNameExceptionException extends InvalidValueException {
    private static final String MESSAGE = "유효하지 않은 지하철 노선명입니다.";

    public InvalidLineNameExceptionException() {
        super(MESSAGE);
    }
}
