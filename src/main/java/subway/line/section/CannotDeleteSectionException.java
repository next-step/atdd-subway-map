package subway.line.section;

public class CannotDeleteSectionException extends Exception{
    public CannotDeleteSectionException() {
        super();
    }

    public CannotDeleteSectionException(String message) {
        super(message);
    }
}
