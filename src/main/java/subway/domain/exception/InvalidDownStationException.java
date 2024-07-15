package subway.domain.exception;

public class InvalidDownStationException extends SubwayDomainException {
    public InvalidDownStationException(Long id) {
        super(SubwayDomainExceptionType.INVALID_DOWN_STATION, "downStationId " + id + " is invalid");
    }
}
