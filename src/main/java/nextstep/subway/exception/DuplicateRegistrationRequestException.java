package nextstep.subway.exception;

public class DuplicateRegistrationRequestException extends RuntimeException {

    public DuplicateRegistrationRequestException() {
        super();
    }

    public DuplicateRegistrationRequestException(String message) {
        super(message);
    }
}
