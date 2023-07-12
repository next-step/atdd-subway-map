package subway.exception;

public enum ErrorMessage {
    NOT_FOUND_SUBWAY_LINE_ID("존재하지 않는 지하철 노선 ID 입니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
