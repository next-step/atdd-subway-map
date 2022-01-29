package nextstep.subway.exception;

public class DistanceUnderZeroException extends RuntimeException {

    private static final String MESSAGE = "Distance 의 값이 0 이하 입니다. [value = %d]";

    public DistanceUnderZeroException(int distane) {
        super(String.format(MESSAGE, distane));
    }
}
