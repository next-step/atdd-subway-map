package nextstep.subway.line.exceptions;

public class ExistingLineException extends RuntimeException {
    private static final String MESSAGE = "이미 존재하는 노선의 이름입니다.";

    public ExistingLineException() {
        super(MESSAGE);
    }
}
