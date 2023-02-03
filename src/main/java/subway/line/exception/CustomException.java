package subway.line.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomException extends RuntimeException{
    public static final String ALREADY_CREATED_SECTION_MSG = "추가하려는 구간이 기존에 등록되어 있는 구간입니다.";
    public static final String CAN_CREATE_ONLY_LAST_SECTION_MSG = "하행 종점 구간만 구간 생성이 가능합니다.";
    public static final String CAN_DELETE_ONLY_LAST_SECTION_MSG = "하행 종점 구간만 구간 삭제가 가능합니다.";
    public static final String INVALID_DISTANCE_MSG = "구간의 거리는 0보다 커야합니다.";
    public static final String EMPTY_SECTIONS_IN_LINE = "노선에 구간이 존재하지 않습니다.";

    public CustomException(String message) {
        super(message);
    }
}
