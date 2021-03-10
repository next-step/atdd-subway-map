package nextstep.subway.line.exception;

import nextstep.subway.common.SystemException;

public class LineCreateFailException extends SystemException {

    public LineCreateFailException(String message, Object ...args) {
        super(message, args);
    }

    public static LineCreateFailException alreadyExist() {
        return new LineCreateFailException("이미 등록된 노선입니다.");
    }

}
