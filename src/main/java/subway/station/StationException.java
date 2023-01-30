package subway.station;

import java.util.NoSuchElementException;

public class StationException extends NoSuchElementException {
    private static final String DEFAULT_MESSAGE = "지하철역 기능 처리중 에러가 발생하였습니다.";

    public StationException() {
        super(DEFAULT_MESSAGE);
    }

    public StationException(String s) {
        super(s);
    }
}
