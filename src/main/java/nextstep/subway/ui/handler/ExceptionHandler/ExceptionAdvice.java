package nextstep.subway.ui.handler.ExceptionHandler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice(basePackages = {"nextstep.subway"})
@RequiredArgsConstructor
public class ExceptionAdvice {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }
}
