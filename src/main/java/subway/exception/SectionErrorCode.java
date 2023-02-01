package subway.exception;

import org.springframework.http.HttpStatus;

public enum SectionErrorCode implements ErrorCode {

	NOT_FOUND_STATION(HttpStatus.NOT_FOUND, "지정한 역을 찾을 수 없습니다."),
	INVALID_SECTION_DISTANCE(HttpStatus.BAD_REQUEST, "잘못된 역간 거리입니다."),
	INVALID_SECTION_UP_STATION(HttpStatus.BAD_REQUEST, "새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다."),
	ALREADY_STATION_REGISTERED(HttpStatus.BAD_REQUEST, "요청한 하행역은 이미 노선에 등록되어 있습니다.");

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
