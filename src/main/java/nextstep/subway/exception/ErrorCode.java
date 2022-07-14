package nextstep.subway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	ALREADY_REGISTER_SECTION(HttpStatus.BAD_REQUEST, "같은 구간에 같은 역이 이미 등록되어 있습니다."),
	CANNOT_REGISTER_WITH_UP_STATION(HttpStatus.BAD_REQUEST, "하행역이 아닌 역을 가지고 구간을 등록할 수 없습니다.");

	private HttpStatus status;
	private String message;
}
