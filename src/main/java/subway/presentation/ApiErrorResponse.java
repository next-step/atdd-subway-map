package subway.presentation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import subway.exception.ErrorCode;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiErrorResponse {

	private final String message;

	private final String code;

	public static ApiErrorResponse of(ErrorCode errorCode) {
		return new ApiErrorResponse(errorCode.getMessage(), errorCode.getCode());
	}
}
