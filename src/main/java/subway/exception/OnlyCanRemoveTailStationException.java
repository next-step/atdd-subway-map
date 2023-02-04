package subway.exception;

public class OnlyCanRemoveTailStationException extends CannotRemovableException {
    private static final String MESSAGE = "지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있습니다. 즉, 마지막 구간만 제거할 수 있습니다.";

    public OnlyCanRemoveTailStationException(long stationId) {
        super(MESSAGE);
    }
}
