package subway.common.error;

import java.util.Map;

public class InvalidSectionRequestException extends RuntimeException {
    public InvalidSectionRequestException(String message) {
        super(message);
    }

    public InvalidSectionRequestException(String message, Map<String, String> arguments) {
        super(message + " Details: " + arguments);
    }
}
