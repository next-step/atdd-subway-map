package nextstep.subway.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final String RESOURCE_NOT_FOUND_EXCEPTION = "요청하신 리소스를 찾을 수 없습니다.";

    public ResourceNotFoundException() {
        this(RESOURCE_NOT_FOUND_EXCEPTION);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
