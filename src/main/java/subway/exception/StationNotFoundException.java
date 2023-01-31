package subway.exception;

public class StationNotFoundException extends NotFoundException{
    public StationNotFoundException(Long id) {
        super("Station with id " + id + "is not found.");
    }
}
