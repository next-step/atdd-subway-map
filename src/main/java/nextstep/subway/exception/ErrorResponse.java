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

	public int getStatusCode() {
		return statusCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}

