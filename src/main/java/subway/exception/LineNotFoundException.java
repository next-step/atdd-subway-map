package subway.exception;

public class LineNotFoundException extends RuntimeException {

    private static final String MESSAGE = "해당 지하철 노선이 존재하지 않습니다 (id = %d)";

    public LineNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
