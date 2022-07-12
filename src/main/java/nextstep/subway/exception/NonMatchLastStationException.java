package nextstep.subway.exception;

public class NonMatchLastStationException extends IllegalArgumentException {
    public NonMatchLastStationException(String message) {
        super("non match last station with " + message);
    }
}
