package nextstep.subway.applicaion.exception;

public abstract class ConflictRequestException extends RuntimeException {

	public ConflictRequestException(String message) {
		super(message);
	}
}
