package nextstep.subway.exception;

import nextstep.subway.exception.dto.CommonExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = IllegalUpdatingStateException.class)
    public ResponseEntity<CommonExceptionResponse> handleUpdatingStateException(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(CommonExceptionResponse.builder().exception(e).build());
    }

}
