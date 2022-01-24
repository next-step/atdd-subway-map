package nextstep.subway.exception.station;

public class StationErrorResponse {

    private String message;

    private StationErrorResponse() {
    }

    public StationErrorResponse(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
