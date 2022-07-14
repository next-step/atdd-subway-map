package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class StationAlreadyExistInSectionException extends RuntimeException {
    public StationAlreadyExistInSectionException() {
        super("신규 구간의 하행역은 현재 등록되어있는 역일 수 없습니다.");
    }
}
