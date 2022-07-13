package nextstep.subway.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ErrorMessage {
	ALREADY_REGISTER_SECTION("같은 구간에 같은 역이 이미 등록되어 있습니다."),
	CANNOT_REGISTER_WITH_UPSTATION("하행역이 아닌 역을 가지고 구간을 등록할 수 없습니다.");

	private String message;

	public String value() {
		return this.message;
	}
}
