package subway.exception;

public class SectionConstraintException extends IllegalArgumentException {

    private static final String MESSAGE = "구간 제약조건을 위반했습니다.";

    public SectionConstraintException() {
        super(MESSAGE);
    }
}
