package nextstep.subway.exception;

public class LogicException extends RuntimeException {

    private final LogicError logicError;

    public LogicException(LogicError logicError) {
        this.logicError = logicError;
    }

    public LogicError getLogicError() {
        return logicError;
    }
}
