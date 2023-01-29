package subway.line;

import java.util.NoSuchElementException;

public class LineException extends NoSuchElementException {
    private static final String DEFAULT_MESSAGE = "지하철 노선 기능 처리중 에러가 발생하였습니다.";

    public LineException() {
        super(DEFAULT_MESSAGE);
    }

    public LineException(String s) {
        super(s);
    }
}
