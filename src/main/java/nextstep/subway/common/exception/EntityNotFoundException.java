package nextstep.subway.common.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException() {
        super(ErrorMessage.ENTITY_NOT_FOUND.getMessage());
    }
}
