package subway.common.exception;

public enum ErrorCode {
    STATION_NOT_FOUND_EXCEPTION(404, "없는 지하철역입니다."),
    LINE_NOT_FOUND_EXCEPTION(404, "없는 지하철 노선입니다.")
    ;

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
