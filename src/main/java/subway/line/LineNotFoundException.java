package subway.line;

public class LineNotFoundException extends RuntimeException {

    public LineNotFoundException(Long id) {
        super(String.format("Line id {} not found.", id));
    }
}
