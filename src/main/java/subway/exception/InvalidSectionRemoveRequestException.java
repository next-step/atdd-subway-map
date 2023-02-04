package subway.exception;

public class InvalidSectionRemoveRequestException extends SubwayException {

	public InvalidSectionRemoveRequestException(ErrorCode errorCode) {
		super(errorCode);
	}
}
