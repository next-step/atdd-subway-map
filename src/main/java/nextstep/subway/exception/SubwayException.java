package nextstep.subway.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubwayException extends RuntimeException {
    private static final String SEPARATOR = " :::::::: ";

    public SubwayException(String message) {
        super(message);
    }

    public SubwayException(String message, Object org) {
        super(message + makeValueFormat(org));
    }

    private static String makeValueFormat(Object org) {
        return SEPARATOR + "[Value = " + org + "]";
    }

}
