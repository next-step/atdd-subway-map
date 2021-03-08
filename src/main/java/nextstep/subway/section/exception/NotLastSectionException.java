package nextstep.subway.section.exception;

public class NotLastSectionException extends RuntimeException {
    public static final String MESSAGE = "지하철 노선의 마지막 구간이 아닌 경우 삭제할 수 없습니다.";

    public NotLastSectionException() {
        super(MESSAGE);
    }
}
