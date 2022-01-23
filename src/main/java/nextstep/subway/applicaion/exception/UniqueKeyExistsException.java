package nextstep.subway.applicaion.exception;

import static java.lang.String.format;

public class UniqueKeyExistsException extends ConflictRequestException {

	public UniqueKeyExistsException(String existsUniqueKey) {
		super(format("Unique Key '%s' is exists", existsUniqueKey));
	}
}
