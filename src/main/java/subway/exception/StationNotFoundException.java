package subway.exception;

public class StationNotFoundException extends RuntimeException {
    private static final String message = "존재하지 않는 지하철역입니다.";

    public StationNotFoundException() {
        super(message);
    }
}
