package nextstep.subway.common;

import nextstep.subway.exception.DuplicatedLineException;
import nextstep.subway.exception.DuplicatedStationException;
import nextstep.subway.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicatedStationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedStationNameException(DuplicatedStationException ex){
        ErrorResponse body = new ErrorResponse(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(body);
    }

    @ExceptionHandler(DuplicatedLineException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedLineException(DuplicatedLineException ex) {
        ErrorResponse body = new ErrorResponse(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(body);
    }

}
