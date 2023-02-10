package subway.station;

public class StationNotFoundException extends RuntimeException {
    public StationNotFoundException(Long id) {
        super(String.format("Station id {} not found.", id));
    }
}
