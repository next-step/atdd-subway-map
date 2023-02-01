package subway.exception;

public class CannotRemoveUniqueSectionException extends RuntimeException {

    private static final String MESSAGE = "현재 지하철 노선에 등록된 구간이 하나이므로, 지하철 구간을 제거할 수 없습니다.";

    public CannotRemoveUniqueSectionException() {
        super(MESSAGE);
    }
}
