package nextstep.subway.ui;

import nextstep.subway.applicaion.dto.ExceptionResponse;
import nextstep.subway.exception.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> runtimeExceptionHandler(RuntimeException e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError().body(new ExceptionResponse(e.getMessage()));
    }

    // Exception을 custom하는 이유가 이것인지
    // 보통 예외처리할 때 internalServerError를 썼었는데 다른 리뷰를 보니 badRequest를 추천하더라 이유는
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> badRequestExceptionHandler(BadRequestException e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
    }
}
