package nextstep.subway.exception;

public class SubwayException extends RuntimeException {

	public SubwayException(SubwayExceptionMessage exceptionMessage) {
		super(exceptionMessage.msg());
	}

}
