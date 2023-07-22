package subway.global.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static class ExceptionResponse{
        private String message;

        public ExceptionResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse checkBusinessException(BusinessException exception){
        return new ExceptionResponse(exception.getMessage());
    }
}
