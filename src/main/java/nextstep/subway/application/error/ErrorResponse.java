package nextstep.subway.application.error;

public class ErrorResponse {
	private String message;

	private ErrorResponse() {
	}

	private ErrorResponse(String message) {
		this.message = message;
	}

	public static ErrorResponse from(String message) {
		return new ErrorResponse(message);
	}

	public String getMessage() {
		return message;
	}
}
