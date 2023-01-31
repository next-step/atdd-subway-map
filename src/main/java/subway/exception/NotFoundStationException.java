package subway.exception;

public class NotFoundStationException extends SubwayException {

	public NotFoundStationException(ErrorCode errorCode) {
		super(errorCode);
	}
}
