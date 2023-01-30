package subway.exception;

public class StationNotFoundException extends RuntimeException {
    public StationNotFoundException() {
        super("지하철역을 찾을 수 없습니다.");
    }
}
