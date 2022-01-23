package nextstep.subway.exception;

public class DuplicatedLineException extends RuntimeException {

    private static final String MESSAGE = "노선 이름이 중복 됩니다.";

    public DuplicatedLineException() {
        super(MESSAGE);
    }

}

