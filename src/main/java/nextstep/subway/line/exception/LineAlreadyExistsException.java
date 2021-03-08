package nextstep.subway.line.exception;

public class LineAlreadyExistsException extends RuntimeException{
    private static final String message = "이미 존재하는 라인입니다.";

    public LineAlreadyExistsException() {
        super(message);
    }
}
