package subway.common.error;

import java.util.Map;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Long id) {
        super(String.format("Entity not found with id: %d", id));
    }

    public NotFoundException(Map<String, String> arguments) {
        super("Entity not found. Details: " + arguments);
    }
}
