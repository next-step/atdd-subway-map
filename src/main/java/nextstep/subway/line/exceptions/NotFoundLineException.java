package nextstep.subway.line.exceptions;

public class NotFoundLineException extends NullPointerException {
    private static final String MESSAGE = "존재하지 않는 지하철 노선에 대한 요청입니다.";

    public NotFoundLineException() {
        super(MESSAGE);
    }
}
