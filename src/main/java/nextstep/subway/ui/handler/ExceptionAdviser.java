package nextstep.subway.ui.handler;

import nextstep.subway.exception.DuplicateLineException;
import nextstep.subway.exception.DuplicateStationException;
import nextstep.subway.exception.LineNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdviser {

    @ExceptionHandler(LineNotFoundException.class)
    public ResponseEntity<Void> lineNotFoundHandler(LineNotFoundException exception) {
        return ResponseEntity.notFound()
            .build();
    }

    @ExceptionHandler(DuplicateStationException.class)
    public ResponseEntity<Void> duplicateStationHandler(DuplicateStationException exception) {
        return ResponseEntity.badRequest()
            .build();
    }

    @ExceptionHandler(DuplicateLineException.class)
    public ResponseEntity<Void> DuplicateLineHandler(DuplicateLineException exception) {
        return ResponseEntity.badRequest()
            .build();
    }

}
