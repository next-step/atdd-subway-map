package subway.exception.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import subway.exception.LineException;
import subway.exception.SectionException;
import subway.exception.dto.ErrorResponse;

@ControllerAdvice
public class SubwayExceptionController {

    @ExceptionHandler(LineException.class)
    protected ResponseEntity<ErrorResponse> handleLineException(LineException e) {
        return ResponseEntity
            .status(e.getStatusCode())
            .body(new ErrorResponse(e.getStatusCode(), e.getMessage()));
    }

    @ExceptionHandler(SectionException.class)
    protected  ResponseEntity<ErrorResponse> handleSectionException(SectionException e) {
        return ResponseEntity
            .status(e.getStatusCode())
            .body(new ErrorResponse(e.getStatusCode(), e.getMessage()));
    }

}
