package nextstep.subway.line.exception;

import nextstep.subway.common.exception.BusinessException;

public class NotFoundLineException extends BusinessException {

	public static final String MESSAGE_FORMAT = "%s에 해당하는 노선이 없습니다.";

	public NotFoundLineException(Long lineId) {
		super(String.format(MESSAGE_FORMAT, lineId));
	}
}
