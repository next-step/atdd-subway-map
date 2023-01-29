package subway.exception;

public class LineNotFoundException extends NotFoundException {
    public LineNotFoundException(long lineId) {
        super("line with id " + lineId + " is not found.");
    }
}
