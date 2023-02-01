package subway.exception;

public class NotFoundSectionException extends SubwayException {

	public NotFoundSectionException(ErrorCode errorCode) {
		super(errorCode);
	}
}
