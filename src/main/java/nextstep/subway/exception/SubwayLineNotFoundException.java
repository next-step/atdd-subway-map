package nextstep.subway.exception;

public class SubwayLineNotFoundException extends RuntimeException {
    public SubwayLineNotFoundException(Long id) {
        super("not found subway line id by " + id);
    }
}
