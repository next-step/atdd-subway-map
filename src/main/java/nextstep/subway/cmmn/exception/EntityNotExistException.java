package nextstep.subway.cmmn.exception;

//TODO : Custom Exception 공부
public class EntityNotExistException extends RuntimeException {
    private final String message;
    public EntityNotExistException(String message) {
        this.message = message;
    }
}
