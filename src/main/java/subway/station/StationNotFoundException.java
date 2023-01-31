package subway.station;

public class StationNotFoundException extends RuntimeException {
    private static final String message = "지하철역을 찾을 수 없습니다.";

    public StationNotFoundException() {
        super(message);
    }
}