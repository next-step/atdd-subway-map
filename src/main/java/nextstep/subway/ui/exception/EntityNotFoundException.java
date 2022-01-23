package nextstep.subway.ui.exception;

import static java.lang.String.*;

public class EntityNotFoundException extends BadRequestException {

	public EntityNotFoundException(long id) {
		super(format("Entity key '%s' is not exists", id));
	}
}
