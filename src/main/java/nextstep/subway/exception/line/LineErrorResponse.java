package nextstep.subway.exception.line;

public class LineErrorResponse {

    private final String message;

    public LineErrorResponse(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
