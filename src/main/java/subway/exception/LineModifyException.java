package subway.exception;

public class LineModifyException extends IllegalArgumentException{
    private static final String MESSAGE = "노선 수정에 실패하였습니다.";

    public LineModifyException() {
        super(MESSAGE);
    }
}
