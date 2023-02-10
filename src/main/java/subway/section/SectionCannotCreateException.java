package subway.section;

public class SectionCannotCreateException extends RuntimeException {
    public SectionCannotCreateException(String message) {
        super(message);
    }

    public SectionCannotCreateException(Long id) {
        super(String.format("Cannot create section.", id));
    }
}
