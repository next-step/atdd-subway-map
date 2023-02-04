package subway.exception;

public class MinimumLineException extends CannotRemovableException{
    private static final String MESSAGE = "지하철노선이 하나의 구간만 존재할 경우, 역을 삭제할 수 없습니다.";

    public MinimumLineException(long stationId) {
        super(MESSAGE);
    }
}
