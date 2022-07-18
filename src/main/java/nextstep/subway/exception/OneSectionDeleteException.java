package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OneSectionDeleteException extends RuntimeException{
    public OneSectionDeleteException() {
        super("지하철 노선이 상행 종점역과 하행 종점역만 있는 하나의 구간인 경우, 역을 삭제할 수 없습니다.");
    }
}
