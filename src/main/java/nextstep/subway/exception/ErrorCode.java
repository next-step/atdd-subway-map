package nextstep.subway.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	ENTITY_NOT_FOUND(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Entity not founc"),
	LAST_STATION_NOT_MATCH_UP_STATION(HttpStatus.BAD_REQUEST.value(), "하행 종점이 추가하려는 구간의 상행역과 일치하지 않습니다."),
	ALREADY_CONTAINS_STATION(HttpStatus.BAD_REQUEST.value(), "추가하려는 구간의 하행이 이미 노선에 추가되어 있습니다."),
	IS_NOT_LAST_STATION(HttpStatus.BAD_REQUEST.value(), "마지막 구간이 아닙니다."),
	IS_ONLY_SECTION(HttpStatus.BAD_REQUEST.value(), "1개뿐인 구간은 삭제할 수 없습니다.");

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
