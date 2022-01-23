package nextstep.subway.exception;

public class ExceptionResponse {

	private int status;
	private String message;

	public ExceptionResponse(int status, String message) {
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
}
