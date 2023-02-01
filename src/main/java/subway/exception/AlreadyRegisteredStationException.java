package subway.exception;

public class AlreadyRegisteredStationException extends SubwayException {

	public AlreadyRegisteredStationException(ErrorCode errorCode) {
		super(errorCode);
	}
}
