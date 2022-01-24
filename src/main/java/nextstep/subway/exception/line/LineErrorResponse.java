package nextstep.subway.exception.line;

public class LineErrorResponse {

    private String message;

    private LineErrorResponse() {
    }

    public LineErrorResponse(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
