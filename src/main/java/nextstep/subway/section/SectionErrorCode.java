package nextstep.subway.section;

import nextstep.subway.exception.ErrorCode;

public enum SectionErrorCode implements ErrorCode {
	INVALID_UP_STATION,
	INVALID_DOWN_STATION,
	MINIMUM_SECTION_COUNT,
	NOT_INCLUDED_STATION
}
