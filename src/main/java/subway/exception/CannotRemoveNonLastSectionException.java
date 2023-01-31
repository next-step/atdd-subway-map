package subway.exception;

public class CannotRemoveNonLastSectionException extends RuntimeException {

    private static final String MESSAGE = "지하철 노선에 등록된 마지막 구간만 제거할 수 있습니다.";

    public CannotRemoveNonLastSectionException() {
        super(MESSAGE);
    }
}
