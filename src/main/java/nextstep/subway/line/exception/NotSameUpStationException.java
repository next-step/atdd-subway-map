package nextstep.subway.line.exception;

import nextstep.subway.common.exception.BusinessException;

public class NotSameUpStationException extends BusinessException {

	public static final String MESSAGE_FORMAT = "추가되는 상행역 %s은/는 마지막 하행역 %s와/과 같지 않습니다.";
	public NotSameUpStationException(String upStation, String lastStation) {
		super(String.format(MESSAGE_FORMAT, upStation, lastStation));
	}
}
