package subway.line.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.line.domain.exception.SectionAddFailException;
import subway.line.domain.exception.SectionRemoveFailException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SectionAddFailException.class)
    public ResponseEntity<String> handleSectionAddFail(SectionAddFailException exception) {
        return ResponseEntity.unprocessableEntity()
            .body(exception.getMessage());
    }

    @ExceptionHandler(SectionRemoveFailException.class)
    public ResponseEntity<String> handleSectionRemoveFail(SectionRemoveFailException exception) {
        return ResponseEntity.unprocessableEntity()
            .body(exception.getMessage());
    }
}
