package subway.exception.statusmessage;

import org.springframework.http.HttpStatus;

public enum SubwayExceptionStatus {

    SECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "구간을 찾을 수 없습니다."),
    SECTION_NOT_DELETE(HttpStatus.BAD_REQUEST, "구간을 삭제할 수 없습니다."),
    SECTION_NOT_ADD(HttpStatus.BAD_REQUEST, "구간을 추가할 수 없습니다."),
    LINE_NOT_FOUND(HttpStatus.NOT_FOUND, "노선을 찾을 수 없습니다."),
    STATION_NOT_FOUND(HttpStatus.NOT_FOUND, "역을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    SubwayExceptionStatus(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
