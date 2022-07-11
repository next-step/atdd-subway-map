package nextstep.subway.exception;

public class SectionNotFoundException extends RuntimeException {
    public SectionNotFoundException(Long id) {
        super("not found section id by " + id);
    }
}
