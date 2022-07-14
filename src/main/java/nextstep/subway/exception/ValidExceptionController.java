package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @author a1101466 on 2022/07/13
 * @project subway
 * @description
 */
@ControllerAdvice
public class ValidExceptionController {
    /**
     * @valid  유효성체크에 통과하지 못하면  MethodArgumentNotValidException 이 발생한다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodValidException(MethodArgumentNotValidException e, HttpServletRequest request){
        ErrorResponse errorResponse = makeErrorResponse(e.getBindingResult());
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse makeErrorResponse(BindingResult bindingResult){
        String message = "";

        //에러가 있다면
        if(bindingResult.hasErrors()){
            //DTO에 설정한 meaasge값을 가져온다
            message = bindingResult.getFieldError().getDefaultMessage();

        }

        return new ErrorResponse(message);
    }
}
