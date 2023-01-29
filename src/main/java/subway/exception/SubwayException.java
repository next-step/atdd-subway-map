package subway.exception;

import lombok.Getter;

@Getter
public abstract class SubwayException extends RuntimeException {

	private final ErrorCode errorCode;

	protected SubwayException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
