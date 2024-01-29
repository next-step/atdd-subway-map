package subway.section;

public class SectionException extends RuntimeException {

    public SectionException() {
    }

    public SectionException(String s) {
        super(s);
    }

    public SectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SectionException(Throwable cause) {
        super(cause);
    }
}
