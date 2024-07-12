package subway.commons;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.MessageFormat;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleHttpException(HttpException exception) {
        if (exception.getField() != null) {
            return new ResponseEntity<>(fieldFormatting(exception.getErrorCode(), exception.getField()), exception.getErrorCode().getHttpStatus());
        }
        return new ResponseEntity<>(exception.getErrorCode().getMessage(), exception.getErrorCode().getHttpStatus());
    }

    private String fieldFormatting(ErrorCode errorCode, String field) {
        return MessageFormat.format(errorCode.getMessage(), field);
    }

}
