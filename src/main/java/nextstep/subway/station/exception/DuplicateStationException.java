package nextstep.subway.station.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class DuplicateStationException extends DataIntegrityViolationException {
    public DuplicateStationException(String msg) {
        super(msg);
    }
}
