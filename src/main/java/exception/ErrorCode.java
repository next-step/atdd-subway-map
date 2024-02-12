package exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public enum ErrorCode {
    NOT_EXIST_STATION(BAD_REQUEST, "존재하지 않는 역입니다."),
    LINE_INCLUDE_NEW_SECTION_DOWNSTATION(BAD_REQUEST, "새로운 구간의 하행역이 이미 노선에 등록된 역입니다"),
    NOT_MATCH_STATION(BAD_REQUEST, "새로운 구간의 상행역과 노선의 하행역이 일치하지 않습니다.");

    private final HttpStatus status;
    private final String message;

    // 생성자
    private ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    // getter 메서드
    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
