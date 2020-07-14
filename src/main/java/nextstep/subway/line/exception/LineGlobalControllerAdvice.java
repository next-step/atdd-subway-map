package nextstep.subway.line.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LineGlobalControllerAdvice {
    @ExceptionHandler(DuplicateStationInLineException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDuplicateStationLineException() {
        return "지하철 역이 해당 노선에 이미 등록되어 있습니다.";
    }

    @ExceptionHandler(NonExistStationInLineException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNonExistStationException() {
        return "존재하지 않는 역은 등록이 불가능 합니다.";
    }
}
