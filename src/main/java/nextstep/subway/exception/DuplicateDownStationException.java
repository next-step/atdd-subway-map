package nextstep.subway.exception;

public class DuplicateDownStationException extends RuntimeException {
    public DuplicateDownStationException() {
        super("duplicate downStationId in subway line stations");
    }
}
