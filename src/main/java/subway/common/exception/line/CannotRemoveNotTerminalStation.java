package subway.common.exception.line;

import subway.common.exception.InvalidValueException;

public class CannotRemoveNotTerminalStation extends InvalidValueException {

    public static final String MESSAGE = "지하철 노선의 하행 종점역만 삭제할 수 있습니다.";

    public CannotRemoveNotTerminalStation() {
        super(MESSAGE);
    }
}
