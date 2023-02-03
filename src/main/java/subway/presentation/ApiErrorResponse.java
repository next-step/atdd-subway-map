package subway.presentation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import subway.exception.ErrorCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiErrorResponse {

	private String message;

	private String code;

	public static ApiErrorResponse of(ErrorCode errorCode) {
		return new ApiErrorResponse(errorCode.getMessage(), errorCode.getCode());
	}
}
