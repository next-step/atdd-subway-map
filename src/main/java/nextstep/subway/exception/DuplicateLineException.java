package nextstep.subway.exception;

public class DuplicateLineException extends RuntimeException {
    private static final String message = "이미 존재하는 노선입니다.";

    public DuplicateLineException() {
        super(message);
    }
}
