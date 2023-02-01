package subway.section;

import java.util.NoSuchElementException;

public class SectionException extends NoSuchElementException {
    private static final String DEFAULT_MESSAGE = "지하철 구간 기능 처리중 에러가 발생하였습니다.";

    public SectionException() {
        super(DEFAULT_MESSAGE);
    }

    public SectionException(String s) {
        super(s);
    }
}
