package nextstep.subway.exception;

public class ErrorResponse {

	private final ErrorCode code;

	public ErrorResponse(CustomException customException) {
		this.code = customException.getErrorCode();
	}

	public ErrorCode getCode() {
		return code;
	}
}
