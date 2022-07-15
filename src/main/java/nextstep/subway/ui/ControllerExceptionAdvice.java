package nextstep.subway.ui;

import lombok.extern.slf4j.Slf4j;
import nextstep.subway.domain.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler({
            InvalidMatchEndStationException.class,
            InvalidUpDownStationException.class,
            OutOfBoundDistanceException.class,
            StationAlreadyExistsException.class,
            SectionDeleteException.class
    })
    public ResponseEntity<Void> badRequest(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({
            NotFoundLineException.class,
            NotFoundStationException.class
    })
    public ResponseEntity<Void> notFound(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> internalServerError(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.internalServerError().build();
    }
}
