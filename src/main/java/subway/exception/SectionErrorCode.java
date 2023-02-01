package subway.exception;

import org.springframework.http.HttpStatus;

public enum SectionErrorCode implements ErrorCode {

	NOT_FOUND_STATION(HttpStatus.NOT_FOUND, "지정한 역을 찾을 수 없습니다."),
	INVALID_SECTION_DISTANCE(HttpStatus.BAD_REQUEST, "잘못된 역간 거리입니다.");

	private final HttpStatus httpStatus;

	private final String message;

	SectionErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	@Override
	public HttpStatus getStatusCode() {
		return this.httpStatus;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public String getCode() {
		return this.name();
	}
}
