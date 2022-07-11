package nextstep.subway.exception;

public class UnableRemoveSectionException extends RuntimeException {
    public UnableRemoveSectionException() {
        super("There is only one subway section. Unable to delete");
    }
}
