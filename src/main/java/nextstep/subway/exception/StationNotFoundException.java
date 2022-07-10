package nextstep.subway.exception;

public class StationNotFoundException extends RuntimeException {
    public StationNotFoundException(Long id) {
        super("not found station id by " + id);
    }
}
