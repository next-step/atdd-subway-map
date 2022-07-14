package nextstep.subway.exception;

public class BusinessException extends RuntimeException {

	private final ErrorCode errorCode;

	public BusinessException(final ErrorCode errorCode) {
		super(errorCode.getErrorMessage());
		this.errorCode = errorCode;
	}

	public int getStatus() {
		return errorCode.getStatusCode();
	}

	public String getErrorMessage() {
		return errorCode.getErrorMessage();
	}
}
