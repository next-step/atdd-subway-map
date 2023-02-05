package subway.section.exception;

import subway.common.exception.SubwayException;

public class SingleSectionException extends SubwayException {

    private static final String MESSAGE ="구간은 2개 이상일 때 삭제할 수 있습니다.";

    public SingleSectionException() {
        super(MESSAGE);
    }
}
