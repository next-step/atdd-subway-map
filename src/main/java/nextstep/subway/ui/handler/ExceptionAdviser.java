package nextstep.subway.ui.handler;

import nextstep.subway.exception.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdviser {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Void> badRequestHandler(BadRequestException exception) {
        return ResponseEntity.badRequest()
            .build();
    }

}
