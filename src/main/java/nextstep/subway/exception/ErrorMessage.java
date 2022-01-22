package nextstep.subway.exception;

public enum ErrorMessage {
    ENTITY_NOT_FOUND("요청하신 데이터를 찾을 수 없습니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String errorMessage() {
        return message;
    }
}
