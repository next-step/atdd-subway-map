package nextstep.subway.line.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class LineNotFoundException extends RuntimeException {
    public LineNotFoundException(Long id) {
        super(String.format("존재하지 않는 노선입니다. id: %d", id));
    }
}
