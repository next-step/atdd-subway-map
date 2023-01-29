package subway.common.exception.line;

import subway.common.exception.InvalidValue;

public class InvalidLineName extends InvalidValue {
    private static final String MESSAGE = "유효하지 않은 지하철 노선명입니다.";

    public InvalidLineName() {
        super(MESSAGE);
    }
}
