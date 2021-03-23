package nextstep.subway.station.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class StationNotFoundException extends RuntimeException {
    public StationNotFoundException(Long id) {
        super(String.format("존재하지 않는 역입니다. id: %d", id));
    }
}
