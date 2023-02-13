package subway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SubwayExceptionHandler {

    @ExceptionHandler(value = SubwayException.class)
    ResponseEntity<ErrorResponse> handlerSubwayException(SubwayException e){
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
            .body(new ErrorResponse(e.getErrorCode().getMessage()));
    }
}
