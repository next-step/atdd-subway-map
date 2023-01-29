package subway.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum SubwayLineErrorCode implements ErrorCode {

	NOT_FOUND_STATION(HttpStatus.NOT_FOUND, "지정한 역을 찾을 수 없습니다.");

	private final HttpStatus httpStatus;

	private final String message;

	SubwayLineErrorCode(HttpStatus httpStatus, String message) {
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
