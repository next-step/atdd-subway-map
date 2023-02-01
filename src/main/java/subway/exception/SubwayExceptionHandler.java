package subway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class SubwayExceptionHandler {

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<?> exceptionHandler(SubwayException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(response);
    }
}
