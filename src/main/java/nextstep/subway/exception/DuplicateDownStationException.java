package nextstep.subway.exception;

public class DuplicateDownStationException extends IllegalArgumentException {
    public DuplicateDownStationException() {
        super("duplicate downStationId in subway line stations");
    }
}
