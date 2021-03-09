package nextstep.subway.station.exception;

import nextstep.subway.common.exception.BusinessException;

public class NotFoundStationException extends BusinessException {

	private static final String MESSAGE_FORMAT = "%s에 해당하는 지하철역이 없습니다.";

	public NotFoundStationException(Long stationId) {
		super(String.format(MESSAGE_FORMAT, stationId));
	}
}
