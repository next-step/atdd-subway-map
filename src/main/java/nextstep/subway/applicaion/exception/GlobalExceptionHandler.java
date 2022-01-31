package nextstep.subway.applicaion.exception;

import nextstep.subway.applicaion.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ElementDuplicateException.class)
    private ResponseEntity<ErrorResponse> handleElementDuplicateException(ElementDuplicateException e) {
        ErrorResponse response = new ErrorResponse(e.getStatusCode(), e.getReason());

        return ResponseEntity
                .status(e.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(NotRegisterDownStationException.class)
    private ResponseEntity<ErrorResponse> handleNotRegisterDownStationException(NotRegisterDownStationException e) {
        ErrorResponse response = new ErrorResponse(e.getStatusCode(), e.getReason());

        return ResponseEntity
                .status(e.getStatusCode())
                .body(response);
    }
}
