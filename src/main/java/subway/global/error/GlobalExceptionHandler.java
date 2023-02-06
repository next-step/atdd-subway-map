package subway.global.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.global.error.exception.BusinessException;

import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비즈니스 로직 실행 중 오류 발생
     */
    @ExceptionHandler(value = { BusinessException.class })
    protected ResponseEntity<ErrorResponse> handleConflict(BusinessException e) {
        List<String> errorMessages = List.of(e.getMessage());
        HttpStatus httpStatus = HttpStatus.valueOf(e.getStatus());
        ErrorResponse errorResponse = ErrorResponse.of(httpStatus, errorMessages);
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * 나머지 예외 발생
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        List<String> errorMessages = List.of(e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, errorMessages);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}