package nextstep.subway.exception;

public class ErrorResponse {
	private int statusCode;
	private String errorMessage;

	private ErrorResponse(int statusCode, String errorMessage) {
		this.statusCode = statusCode;
		this.errorMessage = errorMessage;
	}

	public static ErrorResponse of(int errorCode, String errorMessage) {
		return new ErrorResponse(errorCode, errorMessage);
	}

	public static ErrorResponse from(ErrorCode errorCode) {
		return new ErrorResponse(errorCode.getStatusCode(), errorCode.getErrorMessage());
	}

}

