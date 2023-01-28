package subway.station;

public class StationNotFoundException extends RuntimeException{
    public StationNotFoundException(Long id) {
        super("Station with id " + id + "is not found.");
    }
}
