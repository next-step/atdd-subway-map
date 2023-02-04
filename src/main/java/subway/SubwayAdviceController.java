package subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SubwayAdviceController {

    @ExceptionHandler(CanNotAddSectionException.class)
    public ResponseEntity<Void> exception(CanNotAddSectionException e) {
        return ResponseEntity.unprocessableEntity().build();
    }
}
