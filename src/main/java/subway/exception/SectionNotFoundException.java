package subway.exception;

public class SectionNotFoundException extends NotFoundException{
    public SectionNotFoundException() {
        super("Section is not found.");
    }
}
