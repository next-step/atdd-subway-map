package nextstep.subway.application.exception;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

import static java.lang.String.format;

public class InvalidSectionRemovalException extends BadRequestException {

	public InvalidSectionRemovalException(Line line) {
		super(format("line (%s) has only one section", line.getName()));
	}

	public InvalidSectionRemovalException(Station station) {
		super(format("station (%s) is not last down station", station.getName()));
	}
}
