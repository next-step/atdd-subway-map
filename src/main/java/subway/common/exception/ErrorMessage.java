package subway.common.exception;

public enum ErrorMessage {
    NOT_FOUND_LINE("없는 라인 정보입니다."), NOT_FOUND_STATION("없는 역 정보입니다.");
    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
