package subway.exception;

import subway.domain.SubwayLine;

public class NotFoundSectionInSubwayLineException extends SubwayException {

	public NotFoundSectionInSubwayLineException(ErrorCode errorCode) {
		super(errorCode);
	}
}
