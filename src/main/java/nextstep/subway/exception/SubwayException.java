package nextstep.subway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SubwayException extends RuntimeException {
	private final String errorMessage;
}
