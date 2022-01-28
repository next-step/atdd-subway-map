package nextstep.subway.application.exception;

import static java.lang.String.format;

public class UpStationInvalidException extends BadRequestException {
	public UpStationInvalidException(String stationName) {
		super(format("Up station '%s' must be the last station of the line", stationName));
	}
}
