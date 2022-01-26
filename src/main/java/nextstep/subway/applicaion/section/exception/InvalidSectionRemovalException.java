package nextstep.subway.applicaion.section.exception;

import nextstep.subway.applicaion.exception.BadRequestException;
import nextstep.subway.applicaion.line.domain.Line;
import nextstep.subway.applicaion.station.domain.Station;

import static java.lang.String.format;

public class InvalidSectionRemovalException extends BadRequestException {

	public InvalidSectionRemovalException(Line line) {
		super(format("line (%s) has only one section", line.getName()));
	}

	public InvalidSectionRemovalException(Station station) {
		super(format("station (%s) is not last down station", station.getName()));
	}
}
