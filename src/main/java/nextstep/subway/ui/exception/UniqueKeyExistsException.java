package nextstep.subway.ui.exception;

import static java.lang.String.*;

public class UniqueKeyExistsException extends ConflictRequestException {

	public UniqueKeyExistsException(String existsUniqueKey) {
		super(format("Unique Key '%s' is exists", existsUniqueKey));
	}
}
