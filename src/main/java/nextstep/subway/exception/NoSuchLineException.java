package nextstep.subway.exception;

public class NoSuchLineException extends RuntimeException {

    private static final String NO_SUCH_LINE_EXCEPTION = "요청한 노선은 존재하지 않는 노선입니다. (요청한 id: %d)";

    public NoSuchLineException(long lineId) {
        super(String.format(NO_SUCH_LINE_EXCEPTION, lineId));
    }

}