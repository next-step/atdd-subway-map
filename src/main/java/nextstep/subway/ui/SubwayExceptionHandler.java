package nextstep.subway.ui;

import nextstep.subway.domain.exception.LineException;
import nextstep.subway.domain.exception.SectionException;
import nextstep.subway.domain.exception.StationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class SubwayExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({StationException.class, LineException.class, SectionException.class})
    public ResponseEntity<Object> handleSubwayException(Exception e, WebRequest request) {
        return handleExceptionInternal(
                e,
                new SubwayExceptionResponse(e.getMessage()),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    private class SubwayExceptionResponse {
        private final String message;

        public SubwayExceptionResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

}
