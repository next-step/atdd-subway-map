package subway.domain.exception;

public class InvalidStationException extends SubwayDomainException {
    public InvalidStationException(Long id) {
        super(SubwayDomainExceptionType.INVALID_STATION, "stationId " + id + " is invalid");
    }
}
