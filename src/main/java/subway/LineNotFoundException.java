package subway;

public class LineNotFoundException extends RuntimeException {
    public LineNotFoundException(Long id) {
        super(String.format("Line not found with id: %d", id));
    }
}
