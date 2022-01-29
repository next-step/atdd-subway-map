package nextstep.subway.exception;

import javax.servlet.http.HttpServletRequest;
import nextstep.subway.applicaion.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateException.class)
    public ErrorResponse handleDuplicateException(HttpServletRequest request, DuplicateException duplicateException) {
        return ErrorResponse.of(request.getRequestURI(), HttpStatus.CONFLICT,
            duplicateException.getMessage());
    }

}
