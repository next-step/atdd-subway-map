package subway.exception;

public class StationNotFoundException extends SubwayException {
    public static final String EXCEPTION_MESSAGE = "지하철역을 찾을 수 없습니다.";

    public StationNotFoundException() {
        super(EXCEPTION_MESSAGE);
    }
}
