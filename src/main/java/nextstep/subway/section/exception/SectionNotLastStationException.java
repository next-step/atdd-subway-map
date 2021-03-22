package nextstep.subway.section.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "지하철 노선에 등록된 마지막 역(하행 종점역)만 제거할 수 있다.")
public class SectionNotLastStationException extends RuntimeException {
    public SectionNotLastStationException() {
        super("지하철 노선에 등록된 마지막 역(하행 종점역)만 제거할 수 있다.");
    }
}
