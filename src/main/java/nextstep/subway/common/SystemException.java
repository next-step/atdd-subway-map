package nextstep.subway.common;

public class SystemException extends RuntimeException{

    public SystemException(String message, Object ...args) {
        super(String.format(message, args));
    }

}
