package nextstep.subway.exception;

public class StationException {

	public static class DuplicatedNameException extends IllegalArgumentException {
		private static final String DEFAULT_ERROR_MESSAGE = "Duplicate name";

		public DuplicatedNameException() {
			super(DEFAULT_ERROR_MESSAGE);
		}

		public DuplicatedNameException(String message) {
			super(message);
		}
	}
}
