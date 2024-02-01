package subway.service.exception;

import org.springframework.http.HttpStatus;
import subway.common.BusinessException;

public class NotFoundStationException extends BusinessException {
	private final Long id;

	public NotFoundStationException(Long id) {
		super(HttpStatus.NOT_FOUND);
		this.id = id;
	}

	@Override
	public String getMessage() {
		return String.format("%s 역은 존재하지 않습니다.", id);
	}
}
