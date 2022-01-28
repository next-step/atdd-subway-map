package nextstep.subway.application.exception;

public abstract class ConflictRequestException extends RuntimeException {

	public ConflictRequestException(String message) {
		super(message);
	}
}
