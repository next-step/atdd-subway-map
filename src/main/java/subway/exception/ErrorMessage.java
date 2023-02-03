package subway.exception;

public enum ErrorMessage {
    LINE_NOT_FOUND("지하철 호선을 찾을 수 없습니다."),
    STATION_NOT_FOUND("지하철역을 찾을 수 없습니다");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
