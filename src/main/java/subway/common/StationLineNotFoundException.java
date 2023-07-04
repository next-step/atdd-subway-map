package subway.common;

public class StationLineNotFoundException extends RuntimeException {

    public StationLineNotFoundException(Long id) {
        super(String.format("not found station line : %d", id));
    }
}
