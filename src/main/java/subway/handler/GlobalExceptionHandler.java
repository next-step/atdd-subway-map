package subway.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import subway.common.constant.ErrorCode;
import subway.common.dto.ErrorResponse;
import subway.line.exception.LineNotFoundException;
import subway.section.exception.SectionException;
import subway.station.exception.StationNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LineNotFoundException.class)
    public ResponseEntity<ErrorResponse> LineNotFoundExceptionHandler(LineNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.getCollectedErrorResponse(e.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<ErrorResponse> StationNotFoundExceptionHandler(StationNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.getCollectedErrorResponse(e.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SectionException.class)
    public ResponseEntity<ErrorResponse> SectionExceptionHandler(SectionException e) {
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.getCollectedErrorResponse(e.getMessage()));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
