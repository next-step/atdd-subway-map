package subway.common.response;

public enum ErrorCode {
    // Section Add
    INVALID_UP_STATION_ADD(400, "S001", " 새로운 구간의 상행역이 해당 노선에 등록된 하행 종점역이 아닙니다."),
    INVALID_DOWN_STATION_ADD(400, "S002", " 새로운 구간의 하행 종점역이 해당 노선이 포함되어있습니다."),

    //Section Delete
    INSUFFICIENT_STATION_DELETE(400, "S003", " 지하철 노선에 상행 종점역과 하행 종점역만 있습니다."),
    NOT_LAST_STATION_DELETE(400, "S004", " 지하철 노선에 등록된 역(하행 종점역)이 아닙니다.");


    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
