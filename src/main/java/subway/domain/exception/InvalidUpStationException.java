package subway.domain.exception;

public class InvalidUpStationException extends SubwayDomainException {
    public InvalidUpStationException(Long id) {
        super(SubwayDomainExceptionType.INVALID_UP_STATION, "upStationId " + id + " is invalid");
    }
}
