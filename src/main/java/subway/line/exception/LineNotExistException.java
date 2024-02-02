package subway.line.exception;


import subway.common.exception.NotFoundException;

public class LineNotExistException extends NotFoundException {
    public LineNotExistException(final Long id) {
        super(String.format("Line is not exist - id : %s", id));
    }
}
