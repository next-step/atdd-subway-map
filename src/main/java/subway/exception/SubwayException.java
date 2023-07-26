package subway.exception;

public class SubwayException extends RuntimeException {
    public SubwayException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
