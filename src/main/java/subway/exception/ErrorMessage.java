package subway.exception;

public enum ErrorMessage {
    NOT_FOUND_SUBWAY_LINE_ID("존재하지 않는 지하철 노선 ID 입니다."),
    WRONG_UPSTATION_ID("구간의 상행역이 노선의 하행 종점역이 아닙니다."),
    WRONG_DOWNSTATION_ID("구간의 하행역이 노선에 등록된 역일 수 없습니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
