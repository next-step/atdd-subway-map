package common.error.exception;

public enum ErrorCode {

    CANNOT_ADD_SECTION_WITH_INVALID_UP_STATION("추가하려는 구간의 상행역은 노선의 하행 종점역일 수 없습니다.", 422),
    CANNOT_ADD_SECTION_WITH_ALREADY_EXISTS_STATION_IN_LINE("노선에 이미 존재하는 역에 대한 구간은 추가할 수 없습니다.", 422);

    private final String message;
    private final Integer statusCode;

    ErrorCode(final String message, final Integer statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }
}
