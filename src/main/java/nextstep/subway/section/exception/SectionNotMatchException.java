package nextstep.subway.section.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 합니다.")
public class SectionNotMatchException extends RuntimeException {
    public SectionNotMatchException() {
        super("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 합니다.");
    }
}
