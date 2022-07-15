package nextstep.subway.domain.exception;

public class NotFoundSectionException extends IllegalStateException {

    private static final String MESSAGE = "구간을 찾을 수 없습니다.";

    public NotFoundSectionException() {
        super(MESSAGE);
    }
}
