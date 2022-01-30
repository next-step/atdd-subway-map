package nextstep.subway.application.exception;

import nextstep.subway.domain.Station;

import static java.lang.String.format;

public class InvalidSectionRemovalException extends BadRequestException {

	public InvalidSectionRemovalException() {
		super("line has only one section");
	}

	public InvalidSectionRemovalException(Station station) {
		super(format("station (%s) is not last down station", station.getName()));
	}
}
