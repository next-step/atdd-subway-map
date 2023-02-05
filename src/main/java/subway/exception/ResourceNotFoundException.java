package subway.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final String message = "노선 또는 역을 찾을 수 없습니다!";

    public ResourceNotFoundException() {
        super(message);
    }

}
