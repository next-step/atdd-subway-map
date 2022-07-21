package nextstep.subway.exception;

public enum SubwayExceptionMessage {

	// Station
	NOT_EXIST_STATION("존재하지 않는 역입니다."),
	ALREADY_EXIST_STATION("역이 이미 노선에 포함되어 있습니다."),

	// Line
	NOT_EXIST_LINE("존재하지 않는 노선입니다."),

	// Section
	CANNOT_ADD_SECTION("구간을 추가할 수 없습니다."),
	CANNOT_DELETE_SECTION("구간을 삭제할 수 없습니다."),
	ONLY_ONE_SECTION("한 개의 구간만이 존재합니다.");

	private String msg;

	SubwayExceptionMessage(String msg) {
		this.msg = msg;
	}

	public String msg() {
		return msg;
	}
}
