package subway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {


    //404 NOT_FOUND 잘못된 리소스 접근
    LINE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 Line ID 입니다."),
    STATION_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 Station ID 입니다."),
    //500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 에러입니다. 서버 팀에 연락주세요!");

//    ErrorCode(int status, String message) {
//        this.status = status;
//        this.message = message;
//
//    }

    private final int status;
    private final String message;

}

