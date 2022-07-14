package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SectionNotDownEndStationException extends RuntimeException{
    public SectionNotDownEndStationException() {
        super("지하철 노선의 하행 종점역만 삭제 할 수 있습니다.");
    }
}
