package subway.exception;

public class InvalidSectionDistanceException extends BadRequestException{
    public InvalidSectionDistanceException() {
        super("distance of sections must be less than distance of line");
    }
}
