package nextstep.subway.line.exception;

import nextstep.subway.common.SystemException;

public class LineNotFoundException extends SystemException {

    public LineNotFoundException(Long lineId) {
        super("노선을 찾을 수 없습니다.(lineId = %d)", lineId);
    }

}
