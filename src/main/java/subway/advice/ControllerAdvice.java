package subway.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<SubwayException> test(IllegalArgumentException ex) {
		String errorCode = getErrorCode(ex);
		String errorMessage = ex.getMessage();
		return ResponseEntity.badRequest().body(new SubwayException(errorCode, errorMessage));
	}

	private String getErrorCode(IllegalArgumentException ex) {
		String[] error = ex.getClass().toString().split("\\.");
		return error[error.length - 1];
	}

	public static class SubwayException {
		private final String errorCode;
		private final String errorMessage;

		public SubwayException(String errorCode, String errorMessage) {
			this.errorCode = errorCode;
			this.errorMessage = errorMessage;
		}

		public String getErrorCode() {
			return errorCode;
		}

		public String getErrorMessage() {
			return errorMessage;
		}
	}
}
