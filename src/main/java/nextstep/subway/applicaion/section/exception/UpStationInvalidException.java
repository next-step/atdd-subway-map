package nextstep.subway.applicaion.section.exception;

import nextstep.subway.applicaion.exception.BadRequestException;

import static java.lang.String.format;

public class UpStationInvalidException extends BadRequestException {
	public UpStationInvalidException(String stationName) {
		super(format("Up station '%s' must be the last station of the line", stationName));
	}
}
