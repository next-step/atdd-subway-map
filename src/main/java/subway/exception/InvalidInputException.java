package subway.exception;

import lombok.Getter;

@Getter
public class InvalidInputException extends RuntimeException {
    private final String code;
    private final String message;

    public InvalidInputException(String message) {
        super();
        code = "INVALID_INPUT";
        this.message = message;
    }
}
