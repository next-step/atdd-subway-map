package subway.station.exception;

public class StationNotFoundException extends RuntimeException {

    private static final String notFoundMessage = "역이 존재하지 않습니다";

    public StationNotFoundException() {
        super(notFoundMessage);
    }
}
