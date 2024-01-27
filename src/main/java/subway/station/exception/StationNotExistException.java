package subway.station.exception;


public class StationNotExistException extends RuntimeException {
    public StationNotExistException(final Long id) {
        super(String.format("Station is not exist - id : %s", id));
    }
}
