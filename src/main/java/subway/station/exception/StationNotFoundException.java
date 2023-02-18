package subway.station.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StationNotFoundException extends RuntimeException {

    private static final String notFoundMessage = "역이 존재하지 않습니다";

    public StationNotFoundException() {
        super(notFoundMessage);
    }
}
