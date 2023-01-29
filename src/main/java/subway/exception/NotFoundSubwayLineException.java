package subway.exception;

public class NotFoundSubwayLineException extends SubwayException {

	public NotFoundSubwayLineException(ErrorCode errorCode) {
		super(errorCode);
	}
}
