package nextstep.subway.ui;

import nextstep.subway.exception.ContainStationException;
import nextstep.subway.exception.DuplicatedLineException;
import nextstep.subway.exception.NotEqualDownStationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(DuplicatedLineException.class)
    public ResponseEntity<HttpStatus> duplicatedCheck() {
        return ResponseEntity.badRequest().build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotEqualDownStationException.class)
    public ResponseEntity<HttpStatus> checkNotEqualDownStation() {
        return ResponseEntity.badRequest().build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ContainStationException.class)
    public ResponseEntity<HttpStatus> checkContainStation() {
        return ResponseEntity.badRequest().build();
    }
}
