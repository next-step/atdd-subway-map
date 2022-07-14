package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SectionNotMatchedException extends RuntimeException {
    public SectionNotMatchedException() {
        super("새로운 구간 등록시 새로운 구간의 상행역은 기등록된 하행 종점역과 같아야 합니다.");
    }
}
