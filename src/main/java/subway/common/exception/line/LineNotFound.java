package subway.common.exception.line;

import subway.common.exception.EntityNotFound;

public class LineNotFound extends EntityNotFound {

    private static final String MESSAGE = "지하철 노선을 찾을 수 없습니다.";

    public LineNotFound() {
        super(MESSAGE);
    }
}
