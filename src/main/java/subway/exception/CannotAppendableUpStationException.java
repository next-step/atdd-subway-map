package subway.exception;

public class CannotAppendableUpStationException extends SectionException {

    private static final String MESSAGE = "새로운 구간의 상행역은 현재 노선의 하행 종점역이어야 합니다.";

    public CannotAppendableUpStationException(long stationId) {
        super(MESSAGE);
    }
}
