package nextstep.subway.domain.exception;

public class CannotRemoveSectionException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "구간이 하나만 있는 경우 삭제할 수 없습니다.";

    public CannotRemoveSectionException() {
        super(DEFAULT_MESSAGE);
    }
}
