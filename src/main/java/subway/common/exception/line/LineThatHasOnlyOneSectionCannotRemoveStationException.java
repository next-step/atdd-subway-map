package subway.common.exception.line;

import subway.common.exception.InvalidValueException;

public class LineThatHasOnlyOneSectionCannotRemoveStationException extends InvalidValueException {

    public static final String MESSAGE = "구간이 한개뿐인 노선의 하행 종점역은 삭제할 수 없습니다.";

    public LineThatHasOnlyOneSectionCannotRemoveStationException() {
        super(MESSAGE);
    }
}
