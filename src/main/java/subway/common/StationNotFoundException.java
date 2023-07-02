package subway.common;

public class StationNotFoundException extends RuntimeException {

    public StationNotFoundException(Long id) {
        super(String.format("not found station : %d", id));
    }
}
