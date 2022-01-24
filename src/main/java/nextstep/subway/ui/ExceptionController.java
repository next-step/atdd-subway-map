package nextstep.subway.ui;

import nextstep.subway.applicaion.dto.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> runtimeExceptionHandler(RuntimeException e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError().body(new ExceptionResponse(e.getMessage()));
    }
}
