package nextstep.subway.exception;

import nextstep.subway.applicaion.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(value = DuplicationException.class)
    public ResponseEntity<ExceptionResponse> exception(DuplicationException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(exception.getMessage(), exception.getCause().getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }
}
