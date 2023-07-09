package subway.exception;

public class SubwayBadRequestException extends SubwayException {
    public SubwayBadRequestException(final long code, final String message) {
        super(code, message);
    }
}
