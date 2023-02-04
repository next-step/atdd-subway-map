package subway.exception;

public class NonContinuousStationException extends CannotAppendableException {

    private static final String MESSAGE = "새로운 구간의 상행역은 현재 노선의 하행 종점역이어야 합니다.";

    public NonContinuousStationException(long stationId) {
        super(MESSAGE);
    }
}
