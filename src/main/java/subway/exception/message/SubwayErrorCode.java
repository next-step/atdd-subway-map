package subway.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SubwayErrorCode {

    NOT_FOUND_STATION("존재하지 않는 역입니다.");

    String message;
    HttpStatus httpStatus;

    SubwayErrorCode(final String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }
}
