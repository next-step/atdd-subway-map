package nextstep.subway.exception;

public enum ErrorMessage {
    ENTITY_NOT_FOUND("요청하신 데이터를 찾을 수 없습니다.");

    private final String errorMessage;

    ErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String errorMessage() {
        return errorMessage;
    }
}
