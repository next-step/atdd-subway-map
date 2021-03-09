package nextstep.subway.line.exception;

public class OnlyOneSectionLeftCannotBeDeleted extends RuntimeException {

    private static final String EXCEPTION_DESCRIPTION = "구간이 1개인 경우는 삭제할 수 없습니다.";

    public OnlyOneSectionLeftCannotBeDeleted() {
        this(EXCEPTION_DESCRIPTION);
    }

    public OnlyOneSectionLeftCannotBeDeleted(String message) {
        super(message);
    }
}
