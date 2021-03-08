package nextstep.subway.section.exception;

public class OnlyOneSectionException extends RuntimeException {
    public static final String MESSAGE = "한 개의 구간만 있는 경우 삭제할 수 없습니다.";

    public OnlyOneSectionException() {
        super(MESSAGE);
    }
}
