package subway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter

//Exception 처리 공부하면서, 테스트 해보 았습니다. CustomException 은 지양한다고 하네요.
public enum CustomErrorCode implements ErrorCode {

    //404 NOT_FOUND 잘못된 리소스 접근
    LINE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 Line ID 입니다."),
    STATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 Station ID 입니다."),

    //500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다. 서버 팀에 연락주세요!");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
