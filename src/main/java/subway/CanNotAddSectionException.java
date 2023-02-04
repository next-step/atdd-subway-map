package subway;

public class CanNotAddSectionException extends RuntimeException {
    public CanNotAddSectionException(String message) {
        super(message);
    }
}
