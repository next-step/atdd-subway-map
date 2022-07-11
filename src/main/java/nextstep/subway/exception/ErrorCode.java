package nextstep.subway.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	ENTITY_NOT_FOUND(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Entity not founc");

	private int statusCode;
	private String errorMessage;

	ErrorCode(int statusCode, String errorMessage) {
		this.statusCode = statusCode;
		this.errorMessage = errorMessage;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
