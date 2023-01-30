package subway.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.common.DomainException;
import subway.common.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(
            HttpServletRequest request, HttpServletResponse response, DomainException exception) {
        ErrorResponse errorResponse =
                new ErrorResponse(
                        HttpStatus.CONFLICT,
                        exception.getCode(),
                        exception.getMessage(),
                        exception.getData());

        return buildResponseEntity(errorResponse);
    }

    private ResponseEntity<ErrorResponse> buildResponseEntity(ErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
}
