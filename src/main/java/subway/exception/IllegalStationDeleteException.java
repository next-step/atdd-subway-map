package subway.exception;

public class IllegalStationDeleteException extends RuntimeException {
    private static final String message = "구간의 하행종점역을 삭제해야합니다.";

    public IllegalStationDeleteException() {
        super(message);
    }
}
