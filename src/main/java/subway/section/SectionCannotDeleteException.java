package subway.section;

public class SectionCannotDeleteException extends RuntimeException {
    public SectionCannotDeleteException(String message) {
        super(message);
    }

    public SectionCannotDeleteException(Long id) {
        super(String.format("Cannot delete section.", id));
    }
}
