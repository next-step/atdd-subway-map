package subway.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends RuntimeException {
    public GlobalException(String message) {
        super(message);
    }
}
