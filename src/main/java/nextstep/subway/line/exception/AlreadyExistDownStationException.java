package nextstep.subway.line.exception;

import nextstep.subway.common.exception.BusinessException;

public class AlreadyExistDownStationException extends BusinessException {

	public static final String MESSAGE_FORMAT = "추가되는 하행역 %s은/는 이미 존재합니다.";
	public AlreadyExistDownStationException(String downStation) {
		super(String.format(MESSAGE_FORMAT, downStation));
	}
}
