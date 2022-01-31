package nextstep.subway.ui;

public enum ExceptionMessage {
	DEFAULT_MESSAGE("알 수 없는 오류가 발생했습니다."),
	DUPLICATE_VALUE("값이 중복되었습니다."),
	NOT_EXISTS_NOTION("해당 노선에 대한 정보가 없습니다."),
	DUPLICATE_NOTION_NAME("노션 이름이 중복입니다."),
	NOT_EXISTS_STATION("해당 지하철역에 대한 정보가 없습니다."),
	DUPLICATE_STATION_NAME("지하철역 이름이 중복입니다."),
	NOT_ADD_SECTION("구간을 추가할 수 없습니다."),
	NOT_REMOVE_SECTION("구간을 삭제할 수 없습니다")
	;

	private String message;

	ExceptionMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
