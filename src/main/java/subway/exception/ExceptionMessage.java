package subway.exception;

public enum ExceptionMessage {
    UPSTATION_VALIDATION_EXCEPTION("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다."),
    DOWNSTATION_VALIDATION_EXCEPTION("이미 해당 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없습니다."),
    NEW_SECTION_VALIDATION_EXCEPTION("새로운 구간의 상행역과 하행역은 같을 수 없습니다.");

    private String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
