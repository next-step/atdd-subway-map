package subway.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.domain.exception.SubwayDomainException;
import subway.domain.exception.SubwayDomainExceptionType;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ SubwayDomainException.class })
    protected  ResponseEntity<ErrorResponse> handleSubwayException(final SubwayDomainException exception) {
        SubwayDomainExceptionType exceptionType = exception.getExceptionType();
        return ResponseEntity.status(exceptionType.getStatus())
                .body(ErrorResponse.fromSubwayException(exceptionType));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.fromSubwayException(SubwayDomainExceptionType.INTERNAL_SERVER_ERROR));
    }
}
