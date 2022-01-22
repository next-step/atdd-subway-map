package nextstep.subway.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException() {
        super(ErrorMessage.ENTITY_NOT_FOUND.errorMessage());
    }
}
