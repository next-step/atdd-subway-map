package nextstep.subway.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	ENTITY_NOT_FOUND(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Entity not founc"),
	LAST_STATION_NOT_MATCH_UP_STATION(HttpStatus.BAD_REQUEST.value(), "하행 종점이 추가하려는 구간의 상행역과 일치하지 않습니다.");

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
