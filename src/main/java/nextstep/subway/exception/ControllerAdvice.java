package nextstep.subway.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(SectionException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequest(final SectionException e) {
        return ResponseEntity.status(e.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(ExceptionResponse.of(e.getMessage()));
    }
}
