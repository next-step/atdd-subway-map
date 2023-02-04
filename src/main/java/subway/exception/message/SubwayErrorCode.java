package subway.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SubwayErrorCode {

    NOT_FOUND_STATION("존재하지 않는 역입니다."),
    NOT_POSSIBLE_TO_CREATE_STATION("상행역과 하행역이 같은 구간은 생성할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    String message;
    HttpStatus httpStatus;

    SubwayErrorCode(final String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }
}
