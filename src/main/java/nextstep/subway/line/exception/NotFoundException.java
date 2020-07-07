package nextstep.subway.line.exception;

import nextstep.subway.exception.SubwayException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotFoundException extends SubwayException {

    public NotFoundException() {
        super("can not found element");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
