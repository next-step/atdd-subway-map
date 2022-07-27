package nextstep.subway.error.handler;

import nextstep.subway.error.dto.ErrorResponse;
import nextstep.subway.error.exception.LineNotFoundException;
import nextstep.subway.error.exception.SectionAlreadyHasStationException;
import nextstep.subway.error.exception.SectionIsNotLastSequenceOfLine;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LineNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLineNotFoundException(LineNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getErrorMessage());
        return new ResponseEntity(errorResponse, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(SectionIsNotLastSequenceOfLine.class)
    public ResponseEntity<ErrorResponse> handleSectionIsNotLastSequenceOfLineException(SectionIsNotLastSequenceOfLine ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getErrorMessage());
        return new ResponseEntity(errorResponse, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(SectionAlreadyHasStationException.class)
    public ResponseEntity<ErrorResponse> handleSectionAlreadyHasStationExceptionException(SectionAlreadyHasStationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getErrorMessage());
        return new ResponseEntity(errorResponse, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }
}
