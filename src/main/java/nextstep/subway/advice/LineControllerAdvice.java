package nextstep.subway.advice;

import nextstep.subway.exception.UnmatchedLastStationAndNewUpStationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LineControllerAdvice {

    @ExceptionHandler(UnmatchedLastStationAndNewUpStationException.class)
    public ResponseEntity<ErrorResult> unmatchedLastStationAndNewUpStationException(UnmatchedLastStationAndNewUpStationException e) {
        return ResponseEntity.badRequest().body(new ErrorResult(e.getMessage()));
    }
}
