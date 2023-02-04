package subway.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import subway.common.Comment;

public interface ErrorCode {

    String getCode();

    String getMessage();

    HttpStatus getHttpStatus();
}

