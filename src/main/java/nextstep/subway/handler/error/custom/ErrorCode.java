package nextstep.subway.handler.error.custom;

public enum ErrorCode {
    LINE_NOT_FOUND_BY_ID(404, "[ERROR] 해당하는 id의 노선이 존재하지 않습니다."),
    FOUND_DUPLICATED_NAME(409, "[ERROR] 중복된 이름이 존재합니다. "),
    ;

    private final int status;
    private final String detail;

    ErrorCode(int status, String detail) {
        this.status = status;
        this.detail = detail;
    }

    public int getStatus() {
        return status;
    }

    public String getDetail() {
        return detail;
    }
}
