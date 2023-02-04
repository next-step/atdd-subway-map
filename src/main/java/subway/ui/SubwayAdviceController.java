package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.domain.exceptions.CanNotAddSectionException;

@RestControllerAdvice
public class SubwayAdviceController {

    @ExceptionHandler(CanNotAddSectionException.class)
    public ResponseEntity<Void> exception(CanNotAddSectionException e) {
        return ResponseEntity.unprocessableEntity().build();
    }
}
