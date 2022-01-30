package nextstep.subway.application.exception;

import static java.lang.String.format;

public class DownStationInvalidException extends BadRequestException {

	public DownStationInvalidException(String stationName) {
		super(format("Station '%s' already has a section", stationName));
	}
}
