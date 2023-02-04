package subway.exception;

public class CannotAppendableDownStationException extends SectionException {

    private static final String MESSAGE = "새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없습니다.";

    public CannotAppendableDownStationException(long stationId) {
        super(MESSAGE);
    }
}
