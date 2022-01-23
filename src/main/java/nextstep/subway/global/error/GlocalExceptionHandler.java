package nextstep.subway.global.error;

import nextstep.subway.exception.StationNameDuplicationException;
import nextstep.subway.global.error.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlocalExceptionHandler {

    @ExceptionHandler(StationNameDuplicationException.class)
    public ResponseEntity<ErrorResponse> handleStationNameDuplicationException() {
        ErrorResponse response = ErrorResponse.of(ErrorCode.DUPLICATION_STATION_NAME);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
