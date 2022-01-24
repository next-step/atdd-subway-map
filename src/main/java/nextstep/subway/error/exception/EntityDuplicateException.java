package nextstep.subway.error.exception;

public class EntityDuplicateException extends InvalidValueException{
    public EntityDuplicateException() {
        super(ErrorCode.ENTITY_DUPLICATION);
    }
}
