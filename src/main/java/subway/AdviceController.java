package subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.line.LineNotFoundException;
import subway.section.SectionAlreadyExistsException;
import subway.section.SectionCannotCreateException;
import subway.section.SectionCannotDeleteException;
import subway.station.StationNotFoundException;

@RestControllerAdvice
@ControllerAdvice
public class AdviceController {
    @ExceptionHandler(SectionAlreadyExistsException.class)
    public ResponseEntity<Void> exception(SectionAlreadyExistsException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(SectionCannotCreateException.class)
    public ResponseEntity<Void> exception(SectionCannotCreateException e) {
        return ResponseEntity.unprocessableEntity().build();
    }

    @ExceptionHandler(SectionCannotDeleteException.class)
    public ResponseEntity<Void> exception(SectionCannotDeleteException e) {
        return ResponseEntity.unprocessableEntity().build();
    }

    @ExceptionHandler(LineNotFoundException.class)
    public ResponseEntity<Void> exception(LineNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(StationNotFoundException.class)
    public ResponseEntity<Void> exception(StationNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
