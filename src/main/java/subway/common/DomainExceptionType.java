package subway.common;

public enum DomainExceptionType {
    UNKNOWN_RULE("정의되지 않은 Domain 규칙입니다");

    private final String defaultMessage;

    DomainExceptionType(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public String getCode() {
        return this.toString();
    }

    public String getMessage() {
        return defaultMessage;
    }
}
