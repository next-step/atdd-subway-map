package subway.line.exception;


public class LineNotExistException extends RuntimeException {
    public LineNotExistException(final Long id) {
        super(String.format("Line is not exist - id : %s", id));
    }
}
