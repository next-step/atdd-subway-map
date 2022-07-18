package nextstep.subway.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubwayException extends RuntimeException {
    private static final String SEPARATOR = " :::::::: ";

    public SubwayException(String message) {
        super(message);
    }

    public SubwayException(String message, Long ... ids) {
        super(message + makeValueFormat(ids));
    }

    public SubwayException(String message, Object org) {
        super(message + makeValueFormat(org));
    }

    private static String makeValueFormat(Object org) {
        return SEPARATOR + "[Value = " + org + "]";
    }

    private static String makeValueFormat(Long ... ids) {
        return SEPARATOR +
                "[Value = "
                + Arrays.toString(ids)
                + "]";
    }

}
