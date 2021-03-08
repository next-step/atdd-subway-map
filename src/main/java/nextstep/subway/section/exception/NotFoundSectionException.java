package nextstep.subway.section.exception;

public class NotFoundSectionException extends NullPointerException {
    private static final String MESSAGE = "존재하지 않는 지하철 구간에 대한 요청입니다.";

    public NotFoundSectionException() {
        super(MESSAGE);
    }
}
