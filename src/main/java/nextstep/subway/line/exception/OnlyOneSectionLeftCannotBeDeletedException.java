package nextstep.subway.line.exception;

public class OnlyOneSectionLeftCannotBeDeletedException extends RuntimeException {

    private static final String EXCEPTION_DESCRIPTION = "구간이 1개인 경우는 삭제할 수 없습니다.";

    public OnlyOneSectionLeftCannotBeDeletedException() {
        this(EXCEPTION_DESCRIPTION);
    }

    public OnlyOneSectionLeftCannotBeDeletedException(String message) {
        super(message);
    }
}
