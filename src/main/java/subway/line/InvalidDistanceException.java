package subway.line;

import static subway.line.Distance.LOWER_LIMIT;

public class InvalidDistanceException extends SectionException {
    private static final String MESSAGE = "지하철 구간 길이의 하한값은 %d 입니다. (요청값: %d)";

    public InvalidDistanceException(int distance) {
        super(String.format(MESSAGE, LOWER_LIMIT, distance));
    }
}
