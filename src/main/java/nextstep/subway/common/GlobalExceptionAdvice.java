package nextstep.subway.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionAdvice.class);

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e) {
        logger.warn("IllegalStateException", e);
        return makeErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(IllegalArgumentException e) {
        logger.warn("IllegalArgumentException", e);
        return makeErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        logger.error("RuntimeException", e);
        return makeErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        logger.warn("MethodArgumentNotValidException url:{}, trace:{}", request.getRequestURI(), e.getStackTrace());
        return makeValidErrorResponse(e.getBindingResult());
    }

    private ResponseEntity<ErrorResponse> makeValidErrorResponse(BindingResult bindingResult) {
        String message = "valid error";
        if (bindingResult.hasErrors()) {
            message = bindingResult.getFieldError().getDefaultMessage();
        }
        return makeErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> makeErrorResponse(String message, HttpStatus status) {
        return new ResponseEntity(new ErrorResponse(message), status);
    }


    class ErrorResponse {
        private String errorMessage;

        public ErrorResponse(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
