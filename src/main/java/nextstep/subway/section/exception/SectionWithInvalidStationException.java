package nextstep.subway.section.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "새로운 구간의 하행역은 현재 등록되어있는 역일 수 없다.")
public class SectionWithInvalidStationException extends RuntimeException {
    public SectionWithInvalidStationException() {
        super("새로운 구간의 하행역은 현재 등록되어있는 역일 수 없다.");
    }
}
