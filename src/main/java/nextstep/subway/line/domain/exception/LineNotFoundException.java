package nextstep.subway.line.domain.exception;

import nextstep.subway.common.EntityNotFoundException;

public class LineNotFoundException extends EntityNotFoundException {

    public LineNotFoundException(long lineId) {
        super(String.format("Can't find line with id of %d", lineId));
    }
}
