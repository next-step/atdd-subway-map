package nextstep.subway.applicaion.exception;

public class EmptySectionException extends IllegalStateException {
    private final static String MESSAGE = "등록된 구간이 없습니다.";

    public EmptySectionException() {
        super(MESSAGE);
    }
}
