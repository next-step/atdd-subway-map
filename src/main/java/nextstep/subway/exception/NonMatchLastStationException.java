package nextstep.subway.exception;

public class NonMatchLastStationException extends RuntimeException {
    public NonMatchLastStationException(String message) {
        super("non match last station with " + message);
    }
}
