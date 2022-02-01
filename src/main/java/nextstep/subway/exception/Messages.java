package nextstep.subway.exception;

public enum Messages {
    NOT_FOUND("Not Found"),
    DUPLICATE_CREATION("Duplication Creation"),
    ILLEGAL_DELETION("Deletion Forbidden");

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
