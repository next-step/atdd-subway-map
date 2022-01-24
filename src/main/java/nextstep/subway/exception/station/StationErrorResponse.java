package nextstep.subway.exception.station;

public class StationErrorResponse {

    private final String message;

    public StationErrorResponse(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
