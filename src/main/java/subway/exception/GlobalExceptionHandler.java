package subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static subway.exception.CustomErrorCode.INTERNAL_SERVER_ERROR;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ CustomException.class })
    protected ResponseEntity handleCustomException(CustomException e) {
        return new ResponseEntity(new ErrorDto(e.getErrorCode().getHttpStatus(), e.getErrorCode().getMessage()), e.getErrorCode().getHttpStatus());
    }

    //exception 별로 처리 해야 하지만, 테스트용이라 두겠습니다.
    @ExceptionHandler({ Exception.class })
    protected ResponseEntity handleServerException(Exception e) {
        return new ResponseEntity(new ErrorDto(INTERNAL_SERVER_ERROR.getHttpStatus(), INTERNAL_SERVER_ERROR.getMessage()
                + " (" + e.getMessage() +")"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
