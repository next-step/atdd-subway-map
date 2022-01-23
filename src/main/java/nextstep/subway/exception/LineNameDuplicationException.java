package nextstep.subway.exception;

public class LineNameDuplicationException extends RuntimeException {

    private static final String MESSAGE = "%s는 중복된 노선 이름 입니다.";

    public LineNameDuplicationException(String name) {
        super(String.format(MESSAGE, name));
    }
}
