package subway.line;

public class LineNotFoundException extends RuntimeException {
    public LineNotFoundException(long lineId) {
        super("line with id " + lineId + "is not found.");
    }
}
